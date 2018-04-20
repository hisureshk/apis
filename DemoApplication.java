package com.prudential.apigeeshell;

import java.io.File;
import java.io.PrintStream;

import org.jline.utils.AttributedString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@Component
@NoArgsConstructor
@AllArgsConstructor
class API {
	private String name;
	private String version;

}

@Service
class ConsoleService {
	
	private static final String ANSI_RESET = "\\u001B[0m";
	private static final String ANSI_YELLOW = "\\u001B[33m";
	private final PrintStream out = System.out;
	
	public void write (String msg, String...args) {
		this.out.print("> ");
		this.out.print(ANSI_YELLOW);
		this.out.printf(msg, (Object [])args);
		this.out.println(ANSI_RESET);
		this.out.println();
	}
}

@Service
class APIService {
	public void generateAPI(String name, String version){
		StringBuffer sb = new StringBuffer(".\\").append(name).append("-v").append(version).append("\\").append("src\\").append("gateway\\").append(name + "api\\").append("apiproxy");
		File file = new File(sb.toString());
		boolean isCreated = file.mkdirs();
		System.out.println("Creating API with name "+ name + " and version " + version + " is " +  isCreated); 
	}
	
}

@Component 
class ApigeePromptProvider implements PromptProvider {

	@Override
	public AttributedString getPrompt() {
		return new AttributedString("apigee >");
	}
	
	 
}

@ShellComponent
class GenerationCommand {
	
	private final ConsoleService consoleService;
	private final APIService apiService;

	GenerationCommand (APIService apiService, ConsoleService consoleService){
		this.apiService = apiService;
		this.consoleService = consoleService;
	}

	@ShellMethod("Generates Opiniated .")
	public void generate(String name, String version){
		apiService.generateAPI(name, version);
		
		this.consoleService.write("Successfully Created API %s", name);
	}

}
