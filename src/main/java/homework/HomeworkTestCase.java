package homework;


import java.io.File;
import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;


@Retention(RetentionPolicy.RUNTIME)
@interface Question {
	public int value();
}

public class HomeworkTestCase {

	private SortedMap<Integer, String> answers;
	private Map<Class<?>, Object> classesAndInstances; // classes to be tested and their instances

	private int correct;
	private int incorrect;

	public HomeworkTestCase(String resultFile) throws IOException, ReflectiveOperationException, InterruptedException {
		correct = incorrect = 0;
		classesAndInstances = new LinkedHashMap<>();
		answers = new TreeMap<>();

		List<String> lines = Files.lines(Path.of(resultFile)).map(String::strip).collect(Collectors.toList());

		for (String line : lines) {
			if (line.startsWith("TEST") || line.startsWith("EXEC")) { // add a class
				String command = line.substring(5).trim(); // what follows TEST
				String[] comps = command.split("\\s+");
				String className = comps[0];
				String[] args = Arrays.copyOfRange(comps, 1, comps.length);
				args = Arrays.stream(args).map(arg -> arg.replaceAll("###", " ")).collect(Collectors.toList()).toArray(args); //needed to deal with space in some arguments
				Class<?> cls = Class.forName("homework." + className);
				Constructor<?> ctor = cls.getConstructor(String[].class);
				Object object = ctor.newInstance(new Object[] {args});
				if(line.startsWith("TEST"))
						classesAndInstances.put(cls, object);

			} else if (line.startsWith("Question_")) { // store question numbers and answers
				int i = line.indexOf('_');
				int j = line.indexOf(' ');
				int number = Integer.parseInt(line.substring(i + 1, j));
				String answer = line.substring(j + 1).trim().replace("~~~", "\n");
				answers.put(number, answer);
			} else if (line.startsWith("SLEEP")) {
				String duration = line.substring(6).trim();
				try  {
					int number = Integer.parseInt(duration);
					Thread.sleep(number);
				} catch (NumberFormatException ex) {
					System.err.println("Error parsing duration after SLEEP " + duration);
				}
			}
		}
	}

	public void testAll() {
		for (Map.Entry<Class<?>, Object> e : classesAndInstances.entrySet()) {
			System.out.println("Testing " + e.getKey().getName() + " ...");
			Method[] methods = e.getKey().getDeclaredMethods();

			List<Method> questionMethods = Arrays.stream(methods) // consider methods tagged with @Question
					.filter(method -> method.getAnnotationsByType(Question.class).length > 0)
					.collect(Collectors.toList());
			// sort by question order
			Collections.sort(questionMethods,
					Comparator.comparingInt(method -> method.getAnnotation(Question.class).value()));
			for (Method method : questionMethods) {
				int testOrder = method.getAnnotation(Question.class).value();
				Object instance = e.getValue();
				testMethod(method, instance, testOrder);
			}
			System.out.println("--------------------------------");
		}
	}

	private void verboseAssertEqual(String studentResult, String correctResult, int testNumber) {
		if(correctResult.startsWith("$$$")) { // test a file exists
			String dirPath = correctResult.substring(3);
			Path p = Path.of(dirPath, "_SUCCESS");
			if(Files.exists(p)) {
				System.out.printf("Test %s passed\n", testNumber);
				correct++;
			} else  {
				System.out.printf("Test %s incorrect! The file %s was not created successfully\n",testNumber,dirPath);
				incorrect++;
			}
			
		} else {
			boolean comparison = studentResult.equals(correctResult);
			if (comparison) {
				System.out.printf("Test %s passed\n", testNumber);
				correct++;
			} else {
				System.out.printf("Test %s incorrect! Correct result is {%s}. Student result is {%s}\n", testNumber,
						correctResult, studentResult);
				incorrect++;
			}			
		}
			

	}

	public void testMethod(Method question, Object instance, int testNumber) {
		try {
			Object result = question.invoke(instance);
			verboseAssertEqual("" + result, answers.get(testNumber), testNumber);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.err.printf("Test %s EXCEPTION\n", testNumber);
			incorrect++;
			e.printStackTrace();
		}

	}

	public double getGrade() {
		return (correct + 0.0) / (correct + incorrect);
	}

	public int getTotalTests() {
		return correct + incorrect;
	}

	// arguments should be results.txt
	public static void main(String[] args) throws InterruptedException, IOException  {	
		String resultFile = args.length > 0 ? args[0] : "results.txt";

		HomeworkTestCase testCase = null;
		int tests = 0;
		double grade = 0.0;
		try {
			testCase = new HomeworkTestCase(resultFile);
					
			testCase.testAll();
			tests = testCase.getTotalTests();
			grade = testCase.getGrade();
		} catch(IOException e) {
			System.out.println("Could not read the results file, make sure you did not modify the directory structure");
			e.printStackTrace();			
		}  catch (ReflectiveOperationException e) {
			System.out.println("Could not setup the tests, check if your code compiles and runs before running the tests");
			e.printStackTrace();
		} finally {
			System.out.printf("%s tests, grade is %s%%", tests, 100 * grade);
		}
		
		
	}
}