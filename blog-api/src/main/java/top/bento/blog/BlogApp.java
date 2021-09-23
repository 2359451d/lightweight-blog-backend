package top.bento.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;


@SpringBootApplication
public class BlogApp {
    public static void main(String[] args) throws SQLException {
        ConfigurableApplicationContext run = SpringApplication.run(BlogApp.class, args);
        //DataSource bean = run.getBean(DataSource.class);
        //System.out.println(bean);
        //System.out.println(bean.getClass());
    }
}
