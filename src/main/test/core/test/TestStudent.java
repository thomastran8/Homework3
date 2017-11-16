package core.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.api.IAdmin;
import core.api.IInstructor;
import core.api.IStudent;
import core.api.impl.Admin;
import core.api.impl.Instructor;
import core.api.impl.Student;

public class TestStudent {
	
	private IAdmin admin;
	private IInstructor instructor;
	private IStudent student;
	private int currentYear;
	private String instructorName;
	private String className1;
	private String stName1;
	private String hwName1;
	private String hwAns1;
	private int classSize;
	
	@Before
    public void setup() {
		this.admin = new Admin();
		this.instructor = new Instructor();
		this.student = new Student();
		this.currentYear = 2017;
		this.instructorName = "Devanbu";
		this.className1 = "ECS161";
		this.stName1 = "Bob";
		this.hwName1 = "CodeHW";
		this.hwAns1 = "Hello World!";
		this.classSize = 30;
	}
	
	/*
	 * Register class
	 */
	// Able to register student with correct class name, year, instructor name, and class size
	@Test
	public void registerStudentCorrectClassName() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		assertTrue(this.student.isRegisteredFor(stName1, className1, currentYear));
	}
	
	// Should not register student at full capacity [BUG]
	@Test
	public void registerStudentFullCapacityBUG() {
		this.admin.createClass(className1, currentYear, instructorName, 1);
		this.student.registerForClass(stName1, className1, currentYear);
		this.student.registerForClass(stName1, className1, currentYear);
		assertFalse(this.student.isRegisteredFor(stName1, className1, currentYear));
	}
	
	// Should not register student with empty student name [BUG]
	@Test
	public void registerStudentEmptyStudentNameBUG() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass("", className1, currentYear);
		assertFalse(this.student.isRegisteredFor("", className1, currentYear));
	}
	
	// Should not register student into class name that does not exist
	@Test
	public void registerStudentNonexistClassName() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, "Fishing101", currentYear);
		assertFalse(this.student.isRegisteredFor(stName1, "Fishing101", currentYear));
	}
	
	// Should not register student in past [BUG]
	@Test
	public void registerStudentPastBUG() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		currentYear += 1;
		this.student.registerForClass(stName1, className1, currentYear-1);
		assertFalse(this.student.isRegisteredFor(stName1, className1, currentYear-1));
	}
	
	// Able to register for class in present
	@Test
	public void registerStudentCorrect() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		assertTrue(this.student.isRegisteredFor(stName1, className1, currentYear));
	}
	
	// Able to register student in future [BUG]
	@Test
	public void registerStudentFutureBUG() {
		this.admin.createClass(className1, currentYear+1, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear+1);
		assertFalse(this.student.isRegisteredFor(stName1, className1, currentYear+1));
	}
	
	// Student exists in class even when student registered twice
	@Test
	public void registerStudentTwice() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.student.registerForClass(stName1, className1, currentYear);
		assertTrue(this.student.isRegisteredFor(stName1, className1, currentYear));
	}
	
	/*
	 * Submit homework
	 */
	// Able to submit homework with correct student name, homework name, class name, and year
	@Test
	public void studentSubmitClassNameCorrect() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
		this.student.submitHomework(stName1, hwName1, hwAns1, className1, currentYear);
		assertTrue(this.student.hasSubmitted(stName1, hwName1, className1, currentYear));
	}
	
	// Should not submit homework if student is not registered [BUG]
	@Test
	public void UnregisteredStudentSubmitBUG() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
		this.student.submitHomework(stName1, hwName1, hwAns1, className1, currentYear);
		assertFalse(this.student.hasSubmitted(stName1, hwName1, className1, currentYear));
	}
	
	// Should not submit homework with homework name that doesn't exist
	@Test
	public void studentSubmitHomeworkNameIncorrect() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
		this.student.submitHomework(stName1, "PhilosophyHW", hwAns1, className1, currentYear);
		assertFalse(this.student.hasSubmitted(stName1, "PhilosophyHW", className1, currentYear));
	}
	
	// Should not submit homework to class that doesn't exist
	@Test
	public void studentSubmitClassNameIncorrect() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
		this.student.submitHomework(stName1, hwName1, hwAns1, "PizzaMaking", currentYear);
		assertFalse(this.student.hasSubmitted(stName1, hwName1, className1, currentYear));
	}
	
	// Should not submit homework in past [BUG]
	@Test
	public void studentSubmitHwPastBUG() {
		this.admin.createClass(className1, currentYear-1, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.instructor.addHomework(instructorName, className1, currentYear-1, hwName1);
		this.student.submitHomework(stName1, hwName1, hwAns1, className1, currentYear-1);
		assertFalse(this.student.hasSubmitted(stName1, hwName1, className1, currentYear-1));
	}
	
	// Able to submit homework in present
	@Test
	public void studentSubmitHwPresent() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
		this.student.submitHomework(stName1, hwName1, hwAns1, className1, currentYear);
		assertTrue(this.student.hasSubmitted(stName1, hwName1, className1, currentYear));
	}
	
	// Should not submit homework in future [BUG]
	@Test
	public void studentSubmitHwFutureBUG() {
		this.admin.createClass(className1, currentYear+1, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.instructor.addHomework(instructorName, className1, currentYear+1, hwName1);
		this.student.submitHomework(stName1, hwName1, hwAns1, className1, currentYear+1);
		assertFalse(this.student.hasSubmitted(stName1, hwName1, className1, currentYear+1));
	}
	
	// Student able to submit homework twice
	@Test
	public void studentSubmitHwTwice() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
		this.student.submitHomework(stName1, hwName1, hwAns1, className1, currentYear);
		this.student.submitHomework(stName1, hwName1, hwAns1, className1, currentYear);
		assertTrue(this.student.hasSubmitted(stName1, hwName1, className1, currentYear));
	}
	
	// Student should not submit homework when there is no homework added to the class
	@Test
	public void studentSubmitHwWhenNoHw() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.student.submitHomework(stName1, hwName1, hwAns1, className1, currentYear);
		assertFalse(this.student.hasSubmitted(stName1, hwName1, className1, currentYear));
	}
	
	// Student should not submit homework for a class that is not created
	@Test
	public void studentSubmitHwWhenNoClassExist() {
		this.student.registerForClass(stName1, className1, currentYear);
		this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
		this.student.submitHomework(stName1, hwName1, hwAns1, className1, currentYear);
		assertFalse(this.student.hasSubmitted(stName1, hwName1, className1, currentYear));
	}
	
	// Student submit homework with empty homework answer
	@Test
	public void studentSubmitHwEmptyAns() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
		this.student.submitHomework(stName1, hwName1, "", className1, currentYear);
		assertTrue(this.student.hasSubmitted(stName1, hwName1, className1, currentYear));
	}
	
	/*
	 * Drop Class
	 */
	// Able to drop class with correct student name, class name, and current year
	@Test
	public void studentDropClassCorrectClassName() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.student.dropClass(stName1, className1, currentYear);
		assertFalse(this.student.isRegisteredFor(stName1, className1, currentYear));
	}
	
	// Should not drop class with non-existent class name
	@Test
	public void studentDropClassIncorrectClassName() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.student.dropClass(stName1, "FireFighting", currentYear);
		assertTrue(this.student.isRegisteredFor(stName1, className1, currentYear));
	}
	
	// Should not drop class with non-existent student name
	@Test
	public void studentDropClassIncorrectStudentName() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.student.dropClass("Amanda", className1, currentYear);
		assertTrue(this.student.isRegisteredFor(stName1, className1, currentYear));
	}
	
	// Student should not drop class in the past [BUG]
	@Test
	public void studentDropClassPastBUG() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		currentYear += 1;
		this.student.dropClass(stName1, className1, currentYear-1);
		assertTrue(this.student.isRegisteredFor(stName1, className1, currentYear-1));
	}
	
	// Able to drop class in present
	@Test
	public void studentDropClassPresent() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear);
		this.student.dropClass(stName1, className1, currentYear);
		assertFalse(this.student.isRegisteredFor(stName1, className1, currentYear));
	}

	// Able to drop class in future
	@Test
	public void studentDropClassFuture() {
		this.admin.createClass(className1, currentYear+1, instructorName, classSize);
		this.student.registerForClass(stName1, className1, currentYear+1);
		this.student.dropClass(stName1, className1, currentYear+1);
		assertFalse(this.student.isRegisteredFor(stName1, className1, currentYear+1));
	}
	
	// Student drop class when they are not registered for any class
	@Test
	public void studentDropClassUnregistered() {
		this.admin.createClass(className1, currentYear, instructorName, classSize);
		this.student.dropClass(stName1, className1, currentYear);
		assertFalse(this.student.isRegisteredFor(stName1, className1, currentYear));
	}

}
