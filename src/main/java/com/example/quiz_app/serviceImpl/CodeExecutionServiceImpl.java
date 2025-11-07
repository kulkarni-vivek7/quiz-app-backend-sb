package com.example.quiz_app.serviceImpl;

import com.example.quiz_app.dao.QuestionDao;
import com.example.quiz_app.dto.CodeSubmissionDTO;
import com.example.quiz_app.dto.CodeValidationResultDTO;
import com.example.quiz_app.models.Question;
import com.example.quiz_app.service.CodeExecutionService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

@Service
public class CodeExecutionServiceImpl implements CodeExecutionService {

    private final QuestionDao questionDao;
    private static final int TIMEOUT_SECONDS = 5;

    public CodeExecutionServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public CodeValidationResultDTO validateCode(CodeSubmissionDTO submission) {
        CodeValidationResultDTO result = new CodeValidationResultDTO();

        try {
            Question question = questionDao.findByQuestionId(submission.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            if (question.getTestCases() == null || question.getTestCases().isEmpty()) {
                return createErrorResult("No test cases found for this question");
            }

            int totalTestCases = question.getTestCases().size();
            int passedTestCases = 0;
            List<String> testResults = new ArrayList<>();
            String language = submission.getLanguage().toLowerCase();

            for (Question.TestCase testCase : question.getTestCases()) {
                try {
                    String output = executeCode(
                            submission.getCandidateCode(),
                            language,
                            testCase.getInput());

                    boolean isCorrect = normalizeOutput(output).equals(normalizeOutput(testCase.getExpectedOutput()));
                    if (isCorrect) {
                        passedTestCases++;
                        testResults.add(String.format("✓ Passed: input='%s'", testCase.getInput()));
                    } else {
                        testResults.add(String.format("✗ Failed: input='%s', expected='%s', got='%s'",
                                testCase.getInput(), testCase.getExpectedOutput(), output));
                    }
                } catch (Exception e) {
                    testResults.add(String.format("✗ Error: input='%s' - %s",
                            testCase.getInput(), e.getMessage()));
                }
            }

            result.setCorrect(passedTestCases == totalTestCases);
            result.setMessage(String.format("Passed %d out of %d test cases",
                    passedTestCases, totalTestCases));
            result.setTotalTestCases(totalTestCases);
            result.setPassedTestCases(passedTestCases);
            result.setTestResults(testResults);

        } catch (Exception e) {
            return createErrorResult("Error during code execution: " + e.getMessage());
        }

        return result;
    }

    private String executeCode(String code, String language, String input) throws Exception {
        Path tempDir = Files.createTempDirectory("code_exec");
        try {
            String fileName = getFileName(language);
            Path filePath = tempDir.resolve(fileName);
            code = wrapCodeIfNeeded(code, language, input);
            Files.writeString(filePath, code);

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(tempDir.toFile());

            if (language.equals("java")) {
                // Compile Java code
                Process compileProcess = new ProcessBuilder("javac", fileName)
                        .directory(tempDir.toFile())
                        .start();
                int compileStatus = compileProcess.waitFor();
                if (compileStatus != 0) {
                    throw new RuntimeException("Compilation failed: " +
                            new String(compileProcess.getErrorStream().readAllBytes()));
                }
                processBuilder.command("java", "Solution");
            } else if (language.equals("python")) {
                processBuilder.command("python", fileName);
            } else if (language.equals("javascript")) {
                processBuilder.command("node", fileName);
            } else {
                throw new IllegalArgumentException("Unsupported language: " + language);
            }

            Process process = processBuilder.start();

            // Write input if provided
            if (input != null && !input.isEmpty()) {
                try (OutputStream os = process.getOutputStream()) {
                    os.write(input.getBytes());
                    os.flush();
                }
            }

            // Read output with timeout
            CompletableFuture<String> outputFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return new String(process.getInputStream().readAllBytes());
                } catch (IOException e) {
                    throw new RuntimeException("Error reading output", e);
                }
            });

            String output;
            try {
                output = outputFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS).trim();
            } catch (TimeoutException e) {
                process.destroy();
                throw new RuntimeException("Execution timed out after " + TIMEOUT_SECONDS + " seconds");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                String error = new String(process.getErrorStream().readAllBytes());
                throw new RuntimeException("Execution failed: " + error);
            }

            return output;

        } finally {
            // Clean up
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    private String wrapCodeIfNeeded(String code, String language, String input) {
        switch (language) {
            case "java":
                return wrapJavaCode(code);
            case "python":
                return wrapPythonCode(code);
            case "javascript":
                return wrapJavaScriptCode(code);
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }
    }

    private String wrapJavaCode(String code) {
        // If the code already has a main method, return it as is
        if (code.contains("public static void main")) {
            return code;
        }

        // Check if the code has a Solution class
        if (code.contains("class Solution")) {
            // Extract the content between the class braces
            String classContent = code.replaceFirst("(?s).*?class Solution\\s*\\{", "")
                    .replaceFirst("(?s)\\}\\s*$", "").trim();

            return String.format(
                    "import java.util.*;\n" +
                            "public class Solution {\n" +
                            "    %s\n" +  // Original class content\n" +
                            "    \n" +
                            "    public static void main(String[] args) {\n" +
                            "        try (Scanner sc = new Scanner(System.in)) {\n" +
                            "            // Read the first line of input\n" +
                            "            String input = sc.nextLine().trim();\n" +
                            "            \n" +
                            "            // Get all public methods from Solution class\n" +
                            "            java.lang.reflect.Method[] methods = Solution.class.getMethods();\n" +
                            "            \n" +
                            "            // Try to find and call a suitable method\n" +
                            "            for (java.lang.reflect.Method method : methods) {\n" +
                            "                // Only consider methods declared in Solution class\n" +
                            "                if (method.getDeclaringClass() == Solution.class && \n            " +
                            "                    !method.getName().equals(\"main\")) {\n" +
                            "                    try {\n" +
                            "                        // Try with String parameter\n" +
                            "                        if (method.getParameterCount() == 1 && \n            " +
                            "                            method.getParameterTypes()[0] == String.class) {\n" +
                            "                            Object result = method.invoke(\n            " +
                            "                                java.lang.reflect.Modifier.isStatic(method.getModifiers()) ? \n            " +
                            "                                null : new Solution(), input);\n" +
                            "                            if (result != null) {\n" +
                            "                                System.out.print(String.valueOf(result));\n" +
                            "                                return;\n" +
                            "                            }\n" +
                            "                        }\n" +
                            "                        // Try with int parameters (for GCD-like programs)\n" +
                            "                        else if (method.getParameterCount() == 2 && \n            " +
                            "                                 method.getParameterTypes()[0] == int.class &&\n            " +
                            "                                 method.getParameterTypes()[1] == int.class) {\n            " +
                            "                            String[] parts = input.split(\"\\\\s+\");\n" +
                            "                            if (parts.length >= 2) {\n            " +
                            "                                try {\n            " +
                            "                                    int a = Integer.parseInt(parts[0]);\n            " +
                            "                                    int b = Integer.parseInt(parts[1]);\n            " +
                            "                                    Object result = method.invoke(\n            " +
                            "                                        java.lang.reflect.Modifier.isStatic(method.getModifiers()) ? \n            " +
                            "                                        null : new Solution(), a, b);\n            " +
                            "                                    if (result != null) {\n            " +
                            "                                        System.out.print(String.valueOf(result));\n            " +
                            "                                        return;\n            " +
                            "                                    }\n            " +
                            "                                } catch (NumberFormatException e) { \n            " +
                            "                                    // Continue to next method \n            " +
                            "                                }\n            " +
                            "                            }\n" +
                            "                        }\n" +
                            "                    } catch (Exception e) { \n            " +
                            "                        // Continue to next method \n            " +
                            "                    }\n" +
                            "                }\n" +
                            "            }\n" +
                            "            \n" +
                            "            // If we got here, no suitable method was found\n" +
                            "            System.out.println(\"Error: No suitable method found to execute\");\n" +
                            "            \n" +
                            "        } catch (Exception e) {\n" +
                            "            System.out.println(\"Error: \" + \n            " +
                            "                (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));\n" +
                            "        }\n" +
                            "    }\n" +
                            "}",
                    classContent
            );
        }

        // Default wrapper for code without a Solution class
        return String.format(
                "import java.util.*;\n" +
                        "public class Solution {\n" +
                        "    %s\n" +  // Original code\n        "    \n        "    public static void main(String[] args) {\n" +
                        "        try (Scanner sc = new Scanner(System.in)) {\n" +
                        "            // Read the first line of input\n" +
                        "            String input = sc.nextLine().trim();\n" +
                        "            \n" +
                        "            // Process the input as needed\n" +
                        "            System.out.print(input);\n" +
                        "        } catch (Exception e) {\n" +
                        "            System.out.println(\"Error: \" + e.getMessage());\n" +
                        "        }\n" +
                        "    }\n" +
                        "}",
                code
        );
    }

    private String wrapPythonCode(String code) {
        // Extract the first function name from the user's code
        String functionName = extractFirstFunctionName(code);

        // Indent the user's code by 8 spaces
        String indentedCode = indentCode(code, "        ");

        return String.format(
                "import sys\n" +
                        "import json\n" +
                        "\n" +
                        "def process_input(input_str):\n" +
                        "    try:\n" +
                        "        return json.loads(input_str)\n" +
                        "    except (ValueError, TypeError):\n" +
                        "        if (input_str.startswith(\"'\") and input_str.endswith(\"'\")) or \\\n" +
                        "           (input_str.startswith('\"') and input_str.endswith('\"')):\n" +
                        "            return input_str[1:-1]\n" +
                        "        try:\n" +
                        "            return int(input_str)\n" +
                        "        except ValueError:\n" +
                        "            return input_str\n" +
                        "\n" +
                        "if __name__ == \"__main__\":\n" +
                        "    try:\n" +
                        "        input_str = sys.stdin.read().strip()\n" +
                        "        input_data = process_input(input_str)\n" +
                        "        \n" +
                        "%s\n" +
                        "        \n" +
                        "        result = None\n" +
                        "        \n" +
                        "        # Try calling in order of priority\n" +
                        "        if 'solution' in globals() and callable(globals()['solution']):\n" +
                        "            result = solution(input_data)\n" +
                        "        elif '%s' in globals() and callable(globals()['%s']):\n" +
                        "            result = %s(input_data)\n" +
                        "        else:\n" +
                        "            # Fallback: find first user-defined function\n" +
                        "            user_functions = [name for name, obj in globals().items() \n" +
                        "                            if callable(obj) \n" +
                        "                            and not name.startswith('_') \n" +
                        "                            and name not in ['process_input']]\n" +
                        "            if user_functions:\n" +
                        "                result = globals()[user_functions[0]](input_data)\n" +
                        "        \n" +
                        "        if result is not None:\n" +
                        "            print(result)\n" +
                        "    except Exception as e:\n" +
                        "        print(f\"Error: {str(e)}\")\n",
                indentedCode, functionName, functionName, functionName);
    }

    // Helper method to extract the first function name from Python code
    private String extractFirstFunctionName(String code) {
        // Match: def function_name(
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("def\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(");
        java.util.regex.Matcher matcher = pattern.matcher(code);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "solution"; // fallback
    }

    private String extractFirstFunctionNameFromJS(String code) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("function\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(");
        java.util.regex.Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "solution";
    }

    private String indentCode(String code, String indent) {
        String[] lines = code.split("\n");
        StringBuilder indented = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (!line.trim().isEmpty()) {
                indented.append(indent).append(line);
            }
            if (i < lines.length - 1) {
                indented.append("\n");
            }
        }

        return indented.toString();
    }


    private String wrapJavaScriptCode(String code) {
        String functionName = extractFirstFunctionNameFromJS(code);
        return String.format(
                "const readline = require('readline');\n" +
                        "const rl = readline.createInterface({\n" +
                        "    input: process.stdin,\n" +
                        "    output: process.stdout,\n" +
                        "    terminal: false\n" +
                        "});\n" +
                        "\n" +
                        "function processInput(input) {\n" +
                        "    try {\n" +
                        "        return JSON.parse(input);\n" +
                        "    } catch (e) {\n" +
                        "        if ((input.startsWith(\"'\") && input.endsWith(\"'\")) || \n" +
                        "            (input.startsWith('\"') && input.endsWith('\"'))) {\n" +
                        "            return input.slice(1, -1);\n" +
                        "        }\n" +
                        "        const num = parseInt(input, 10);\n" +
                        "        return isNaN(num) ? input : num;\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "let input = '';\n" +
                        "rl.on('line', line => {\n" +
                        "    try {\n" +
                        "        const inputData = processInput(line);\n" +
                        "        \n" +
                        "        %s\n" +
                        "        \n" +
                        "        let result = undefined;\n" +
                        "        \n" +
                        "        if (typeof solution === 'function') {\n" +
                        "            result = solution(inputData);\n" +
                        "        } else if (typeof %s === 'function') {\n" +
                        "            result = %s(inputData);\n" +
                        "        } else {\n" +
                        "            const userFunctions = Object.keys(global).filter(key => typeof global[key] === 'function' && !key.startsWith('_') && !['processInput', 'require', 'console', 'global', 'Buffer', 'process', 'clearTimeout', 'setTimeout'].includes(key));\n" +
                        "            if (userFunctions.length > 0) {\n" +
                        "                result = global[userFunctions[0]](inputData);\n" +
                        "            }\n" +
                        "        }\n" +
                        "        \n" +
                        "        if (result !== undefined) {\n" +
                        "            console.log(result);\n" +
                        "        }\n" +
                        "    } catch (e) {\n" +
                        "        console.error('Error:', e.message);\n" +
                        "        process.exit(1);\n" +
                        "    } finally {\n" +
                        "        rl.close();\n" +
                        "    }\n" +
                        "});",
                code, functionName, functionName);
    }

    private String getFileName(String language) {
        return switch (language) {
            case "java" -> "Solution.java";
            case "python" -> "solution.py";
            case "javascript" -> "solution.js";
            default -> throw new IllegalArgumentException("Unsupported language: " + language);
        };
    }

    private String normalizeOutput(String output) {
        return output.trim().replaceAll("\\r\\n|\\r|\\n", " ").replaceAll("\\s+", " ").trim();
    }

    private CodeValidationResultDTO createErrorResult(String message) {
        CodeValidationResultDTO result = new CodeValidationResultDTO();
        result.setCorrect(false);
        result.setMessage(message);
        result.setTotalTestCases(0);
        result.setPassedTestCases(0);
        result.setTestResults(List.of(message));
        return result;
    }
}