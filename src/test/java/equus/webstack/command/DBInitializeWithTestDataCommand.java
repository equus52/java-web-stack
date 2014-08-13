package equus.webstack.command;

import java.util.HashSet;
import java.util.Set;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;

import com.google.inject.Injector;

import equus.webstack.model.Course;
import equus.webstack.model.Customer;
import equus.webstack.model.Student;
import equus.webstack.service.CourseService;
import equus.webstack.service.CustomerService;
import equus.webstack.service.StudentService;

@Slf4j
public class DBInitializeWithTestDataCommand implements Command {

  @Override
  public Logger getLogger() {
    return log;
  }

  public static void main(String[] args) {
    new DBInitializeWithTestDataCommand().executeCommand(args);
  }

  @Override
  public void execute(String... args) {
    new DBInitializeCommand().execute(injector -> {
      initCustomer(injector);
      initStudent(injector);
      // setup test data
      });
  }

  private void initCustomer(Injector injector) {
    val service = injector.getInstance(CustomerService.class);
    val customer = new Customer();
    customer.setId(0);
    customer.setName("Hiro");
    service.save(customer);

  }

  private void initStudent(Injector injector) {
    Set<Course> courseList = initCourse(injector);
    Set<Student> list = new HashSet<>();
    {
      val entity = new Student();
      entity.setName("student_A");
      entity.setCourseList(courseList);
      list.add(entity);
    }
    {
      val entity = new Student();
      entity.setName("student_B");
      entity.setCourseList(courseList);
      list.add(entity);
    }
    val service = injector.getInstance(StudentService.class);
    for (val entity : list) {
      service.save(entity);
    }
    val list2 = service.findAll();
    for (val entity : list2) {
      for (val c : entity.getCourseList()) {
        System.out.println();
      }
    }
  }

  private Set<Course> initCourse(Injector injector) {
    Set<Course> list = new HashSet<>();
    {
      val entity = new Course();
      entity.setName("course_A");
      list.add(entity);
    }
    {
      val entity = new Course();
      entity.setName("course_B");
      list.add(entity);
    }
    val service = injector.getInstance(CourseService.class);
    for (val entity : list) {
      service.save(entity);
    }
    return list;
  }
}
