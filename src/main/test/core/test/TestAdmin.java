package core.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import core.api.IAdmin;
import core.api.IStudent;
import core.api.impl.Admin;
import core.api.impl.Student;

public class TestAdmin {

    private IAdmin admin;
    private int currentYear;
    private IStudent student;
    private String stName1;
    private String instructorName;
    private int classSize;
    private String className;

    @Before
    public void setup() {
        this.admin = new Admin();
        this.currentYear = 2017;
        this.student = new Student();
        this.stName1 = "Bob";
        this.instructorName = "Devanbu";
        this.classSize = 30;
        this.className = "ECS161";
    }
    
    /*
     * Create Class
     */
    // Should not create class in the past [BUG]
    @Test
    public void createClassYearPastBUG() {
        this.admin.createClass(className, currentYear-1, instructorName, classSize);
        assertFalse(this.admin.classExists(className, currentYear-1));
    }
    
    // Able to create a class in the current year [CORNER]
    @Test
    public void createClassYearCurrentCORNER() {
        this.admin.createClass(className, currentYear, instructorName, classSize);
        assertTrue(this.admin.classExists(className, currentYear));
    }
    
    // Able to create a class in the future year
    @Test
    public void createClassYearFuture() {
        this.admin.createClass(className, currentYear+1, instructorName, classSize);
        assertTrue(this.admin.classExists(className, currentYear+1));
    }
    
    // Able to create a class with correct class name, year, instructor name, and class size
    @Test
    public void createClassClassNameCorrect() {
        this.admin.createClass(className, currentYear, instructorName, classSize);
        assertTrue(this.admin.classExists(className, currentYear));
    }
    
    // Should not create a class with an empty class name [BUG]
    @Test
    public void createClassClassNameEmptyBUG() {
        this.admin.createClass("", currentYear, instructorName, classSize);
        assertFalse(this.admin.classExists("", currentYear));
    }
    
    // Should not create a class with an empty instructor name [BUG]
    @Test
    public void createClassInstructorNameEmptyBUG() {
        this.admin.createClass(className, currentYear, "", classSize);
        assertFalse(this.admin.getClassInstructor(className, currentYear) == "");
    }
    
    // Able to have no class
    @Test
    public void createClassCourseLimitNone() {
    	assertFalse(this.admin.classExists(className, currentYear));
    }
    
    // Able to create a class in the accepted creation limit
    @Test
    public void createClassCourseLimitAcceptable() {
    	this.admin.createClass(className, currentYear, instructorName, classSize);
    	assertTrue(this.admin.classExists(className, currentYear));
    }
    
    // Able to create a class at the max creation limit [CORNER]
    @Test
    public void createClassCourseLimitCORNER() {
    	this.admin.createClass(className, currentYear, instructorName, classSize);
    	this.admin.createClass("Class2", currentYear, instructorName, classSize);
    	assertTrue(this.admin.classExists("Class2", currentYear));
    }
    
    // Should not create a class over the creation limit [BUG]
    @Test
    public void createClassCourseLimitViolationBUG() {
    	this.admin.createClass(className, currentYear, instructorName, classSize);
    	this.admin.createClass("Class2", currentYear, instructorName, classSize);
    	this.admin.createClass("Class3", currentYear, instructorName, classSize);
    	assertFalse(this.admin.classExists("Class3", currentYear));
    }
    
    /*
     * Class capacity
     */
    // Should not create a class capacity less than zero [BUG]
    @Test
    public void classCapacityLessBUG() {
        this.admin.createClass(className, currentYear, instructorName, -1);
        assertFalse(this.admin.getClassCapacity(className,currentYear) < 0);
    }
    
    // Should not create a class capacity at zero [CORNER][BUG]
    @Test
    public void classCapacityCORNERBUG() {
        this.admin.createClass(className, currentYear, instructorName, 0);
        assertFalse(this.admin.getClassCapacity(className,currentYear) == 0);
    }
    
    // Able to create a class capacity greater than zero
    @Test
    public void classCapacityGreater() {
        this.admin.createClass(className, currentYear, instructorName, 1);
        assertTrue(this.admin.getClassCapacity(className,currentYear) > 0);
    }
    
    /*
     * Change class capacity
     */
    // Should not change class capacity to negative [BUG]
    @Test
    public void changeCapacityNegativeBUG() {
        this.admin.createClass(className, currentYear, instructorName, classSize);
        this.admin.changeCapacity(className, currentYear, -1);
        assertFalse(this.admin.getClassCapacity(className,currentYear) < 0);
    }
    
    // Able to change class capacity to zero - given zero students enrolled [CORNER]
    @Test
    public void changeCapacityZeroCORNER() {
        this.admin.createClass(className, currentYear, instructorName, classSize);
        this.admin.changeCapacity(className, currentYear, 0);
        assertTrue(this.admin.getClassCapacity(className,currentYear) == 0);
    }
    
    // Should not change class capacity below number of students enrolled [BUG]
    @Test
    public void changeCapacityBelowEnrolledBUG() {
        this.admin.createClass(className, currentYear, instructorName, classSize);
        this.student.registerForClass(stName1, className, currentYear);
        this.student.registerForClass("Charlie", className, currentYear);
        this.admin.changeCapacity(className, currentYear, 1);
        assertFalse(this.admin.getClassCapacity(className,currentYear) < 2);
    }
    
    // Should not create class with same className and year pair [BUG]
    @Test
    public void createClassMutlipleBUG() {
    	this.admin.createClass(className, currentYear, "Instructor1", classSize);
    	this.admin.createClass(className, currentYear, "Instructor2", 10);
    	assertTrue(this.admin.getClassInstructor(className, currentYear) == "Instructor1");
    }

}
