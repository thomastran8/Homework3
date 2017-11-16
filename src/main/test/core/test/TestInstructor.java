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

public class TestInstructor {
	
	private IAdmin admin;
	private IInstructor instructor;
	private int currentYear;
	private IStudent student;
	private String instructorName;
	private String className1;
	private String hwName1;
	private int classSize;

    @Before
    public void setup() {
        this.instructor = new Instructor();
        this.admin = new Admin();
        this.currentYear = 2017;
        this.student = new Student();
        this.instructorName = "Devanbu";
		this.className1 = "ECS161";
		this.hwName1 = "CodeHw";
		this.classSize = 30;
    }
    
    /*
     * Adding homework
     */
    // Able to add homework class name, year, instructor, and capacity correctly
    @Test
    public void addHwCorrect() {
        this.admin.createClass(className1, currentYear, instructorName, classSize);
        this.instructor.addHomework(instructorName, className1, currentYear, className1);
        assertTrue(instructor.homeworkExists(className1, currentYear, hwName1));
    }
    
    // Able to add homework twice with the same homework name **************************************
    @Test
    public void addHwDuplicate() {
        this.admin.createClass(className1, currentYear, instructorName, classSize);
        this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
        this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
        
        assertTrue(this.instructor.homeworkExists(className1, currentYear, hwName1));
    }
    
    // Able to add homework twice with different homework names
    @Test
    public void addHwDifferent() {
        this.admin.createClass(className1, currentYear, instructorName, classSize);
        this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
        this.instructor.addHomework(instructorName, className1, currentYear, "SortHW");
        
        assertTrue(this.instructor.homeworkExists(className1, currentYear, hwName1));
    }
    
    // Should not add homework with incorrect instructor [BUG]
    @Test
    public void addHwWrongInstructorBUG() {
        this.admin.createClass(className1, currentYear, instructorName, classSize);
        this.instructor.addHomework("RandomPerson", className1, currentYear, hwName1);
        assertFalse(instructor.homeworkExists(className1, currentYear, hwName1));
    }
    
    // Should not add homework with empty instructorName [BUG]
    @Test
    public void AddHwEmptyInstructorNameBUG() {
        this.admin.createClass(className1, currentYear, instructorName, classSize);
        this.instructor.addHomework("", className1, currentYear, hwName1);
        assertFalse(instructor.homeworkExists(className1, currentYear, hwName1));
    }
    
    // Should not add homework with empty homeworkName [BUG]
    @Test
    public void AddHwEmptyHwNameBUG() {
        this.admin.createClass(className1, currentYear, instructorName, classSize);
        this.instructor.addHomework(instructorName, className1, currentYear, "");
        assertFalse(instructor.homeworkExists(className1, currentYear, ""));
    }
    
    /*
     * Assigning grade
     */
    // Should not assign a grade that is negative [BUG]
    @Test
    public void assignGradeNegativeBUG() {
    	this.admin.createClass(className1, currentYear, instructorName, classSize);
    	this.student.registerForClass("Bob", className1, currentYear);
    	this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
    	this.student.submitHomework("Bob", hwName1, "Hello World!", className1, currentYear);
    	this.instructor.assignGrade(instructorName, className1, currentYear, hwName1, "Bob", -10);
    	assertFalse(this.instructor.getGrade(className1, currentYear, hwName1, "Bob") < 0);
    }
    
    // Able to assign a grade of zero [CORNER]
    @Test
    public void assignGradeZero() {
    	this.admin.createClass(className1, currentYear, instructorName, classSize);
    	this.student.registerForClass("Bob", className1, currentYear);
    	this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
    	this.student.submitHomework("Bob", hwName1, "Hello World!", className1, currentYear);
    	this.instructor.assignGrade(instructorName, className1, currentYear, hwName1, "Bob", 0);
    	System.out.println(this.instructor.getGrade(className1, currentYear, hwName1, "Bob"));
    	assertTrue(this.instructor.getGrade(className1, currentYear, hwName1, "Bob") == 0);
    }
    
    // Able to assign grade in acceptable range
    @Test
    public void assignGradeAcceptable() {
    	this.admin.createClass(className1, currentYear, instructorName, classSize);
    	this.student.registerForClass("Bob", className1, currentYear);
    	this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
    	this.student.submitHomework("Bob", hwName1, "Hello World!", className1, currentYear);
    	this.instructor.assignGrade(instructorName, className1, currentYear, hwName1, "Bob", 90);
    	System.out.println(this.instructor.getGrade(className1, currentYear, hwName1, "Bob"));
    	int stGrd = this.instructor.getGrade(className1, currentYear, hwName1, "Bob");
    	assertTrue(stGrd >= 0 || stGrd <= 100);
    }
    
    // Able to assign a grade at the max percentage [CORNER]
    @Test
    public void assignGradeMaxLimitCORNER() {
    	this.admin.createClass(className1, currentYear, instructorName, classSize);
    	this.student.registerForClass("Bob", className1, currentYear);
    	this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
    	this.student.submitHomework("Bob", hwName1, "Hello World!", className1, currentYear);
    	this.instructor.assignGrade(instructorName, className1, currentYear, hwName1, "Bob", 100);
    	System.out.println(this.instructor.getGrade(className1, currentYear, hwName1, "Bob"));
    	assertTrue(this.instructor.getGrade(className1, currentYear, hwName1, "Bob") == 100);
    }
    
    // Able to assign a grade over the max percentage
    @Test
    public void assignGradeOverMax() {
    	this.admin.createClass(className1, currentYear, instructorName, classSize);
    	this.student.registerForClass("Bob", className1, currentYear);
    	this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
    	this.student.submitHomework("Bob", hwName1, "Hello World!", className1, currentYear);
    	this.instructor.assignGrade(instructorName, className1, currentYear, hwName1, "Bob", 120);
    	System.out.println(this.instructor.getGrade(className1, currentYear, hwName1, "Bob"));
    	assertTrue(this.instructor.getGrade(className1, currentYear, hwName1, "Bob") > 100);
    }
    
    // Should not assign a grade with incorrect instructor [BUG]
    @Test
    public void assignGradeWrongInstructorBUG() {
    	this.admin.createClass(className1, currentYear, instructorName, classSize);
    	this.student.registerForClass("Bob", className1, currentYear);
    	this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
    	this.student.submitHomework("Bob", hwName1, "Hello World!", className1, currentYear);
    	this.instructor.assignGrade("RandomPerson", className1, currentYear, hwName1, "Bob", 90);
    	assertNull(this.instructor.getGrade(className1, currentYear, hwName1, "Bob"));
    }
    
    // Should not assign a grade if student hasn't submitted their homework [BUG]
    @Test
    public void assignGradeNoStudentSubmitBUG() {
    	this.admin.createClass(className1, currentYear, instructorName, classSize);
    	this.student.registerForClass("Bob", className1, currentYear);
    	this.instructor.addHomework(instructorName, className1, currentYear, hwName1);
    	this.instructor.assignGrade(instructorName, className1, currentYear, hwName1, "Bob", 90);
    	assertNull(this.instructor.getGrade(className1, currentYear, hwName1, "Bob"));
    }
    
    // Should not assign a grade if homework doesn't exist
    @Test
    public void assignGradeNoHwExist() {
    	this.admin.createClass(className1, currentYear, instructorName, classSize);
    	this.student.registerForClass("Bob", className1, currentYear);
    	this.student.submitHomework("Bob", hwName1, "Hello World!", className1, currentYear);
    	this.instructor.assignGrade(instructorName, className1, currentYear, hwName1, "Bob", 90);
    	assertNull(this.instructor.getGrade(className1, currentYear, hwName1, "Bob"));
    }

}
