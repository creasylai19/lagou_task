//import com.lagou.edu.pojo.Resume;
//import com.lagou.edu.service.IResumeService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.List;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
//public class ResumeServiceTest {
//
//    @Autowired
//    private IResumeService resumeService;
//
//    @Test
//    public void testFindAll(){
//        List<Resume> all = resumeService.findAll();
//        for (Resume resume : all) {
//            System.out.println(resume);
//        }
//    }
//
//    @Test
//    public void testDeleteById(){
//        resumeService.deleteById(4l);
//    }
//
//    @Test
//    public void testCreate(){
//        Resume resume = new Resume();
//        resume.setName("hehe");
//        resume.setAddress("dizhi");
//        resume.setPhone("123456789");
//        resume = resumeService.save(resume);
//        System.out.println(resume);
//    }
//
//    @Test
//    public void testUpdate(){
//        Resume resume = new Resume();
//        resume.setId(4l);
//        resume.setName("修改");
//        resume.setAddress("dizhi");
//        resume.setPhone("123456789");
//        resume = resumeService.save(resume);
//        System.out.println(resume);
//    }
//
//}
