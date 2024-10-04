import files.PayLoad;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class ComplexJsonTest {
    @Test
    public void verifyIfSumOfAllCoursePricesMatchesWithPurchaseAmount(){
        JsonPath json= new JsonPath(PayLoad.getCoursePrice());
        int purchaseAmount= json.getInt("dashboard.purchaseAmount");

        int sum= 0;
        List<Course> courses= json.getList("courses", Course.class);
        System.out.println(courses);

        for(Course course: courses){
            sum += course.price * course.copies;
        }

        Assert.assertEquals(sum, purchaseAmount);
    }
}

class Course{
    public String title;
    public int price;
    public int copies;
}
