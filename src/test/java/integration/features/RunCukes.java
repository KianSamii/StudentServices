package integration.features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by kian on 1/23/16.
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "junit:target/cucumber.xml", "json:target/cucumber.json" },
		tags = {}, features = "src/test/resources/features/students.feature", glue = { "integration" })
public class RunCukes {
}
