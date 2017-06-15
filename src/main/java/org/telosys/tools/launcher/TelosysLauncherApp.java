/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.launcher;

import java.util.List;
import java.util.Scanner;

import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.task.GenerationTaskResult;

public class TelosysLauncherApp {
	
	public static void main(String[] args) throws TelosysToolsException, GeneratorException {

		print("Telosys Generator Launcher" );

		checkArgs(args);
		String  launcherName = args[0] ;
		boolean yesOption = false ;
		if ( args.length == 2 ) {
			if ( "-y".equals(args[1]) ) {
				yesOption = true ;
			}
		}
		print("'yes' option = " + yesOption );

		String workingDirectory = DirUtil.getUserWorkingDirectory() + "/tests-env/project1";
		print("Current working directory : " + workingDirectory );

		print("Creating launcher..." );
		TelosysLauncher launcher = buildLauncher(workingDirectory, launcherName ) ;
		
		print("Launcher ready" );
		print(" - launcher name  : " + launcher.getLauncherName() );
		print(" - launchers dir  : " + launcher.getLaunchersDir() );
		print(" - model name     : " + launcher.getModelName() );
		print(" - bundle name    : " + launcher.getBundleName() );

		print("Telosys configuration : " );
		TelosysToolsCfg telosysToolsCfg = launcher.getTelosysToolsCfg();
		print(" - project path      : " + telosysToolsCfg.getProjectAbsolutePath() );
		print(" - TelosysTools path : " + telosysToolsCfg.getTelosysToolsFolderAbsolutePath() );
		print(" - models folder     : " + telosysToolsCfg.getModelsFolderAbsolutePath() );
		print(" - destination path  : " + telosysToolsCfg.getDestinationFolderAbsolutePath() );

		List<String> entities = launcher.getEntities();
		if ( entities == null ) {
			print("All entities will be generated (no entities selection)" );
		}
		else {
			print(entities.size() + " entities to be generated " );
			for ( String s : entities ) {
				print(" . " + s);
			}
		}

//		testFileExists( "D:\\workspaces-TELOSYS-TOOLS\\wks-43-telosys-tools-bricks\\telosys-tools-api/tests-env/project1" ) ;
//		testFileExists( "D:\\workspaces-TELOSYS-TOOLS\\wks-43-telosys-tools-bricks\\telosys-tools-api/tests-env/project1/TelosysTools" ) ;
//		testFileExists( "D:\\workspaces-TELOSYS-TOOLS\\wks-43-telosys-tools-bricks\\telosys-tools-api/tests-env/project1/TelosysTools/templates" ) ;
//		testFileExists( "D:\\workspaces-TELOSYS-TOOLS\\wks-43-telosys-tools-bricks\\telosys-tools-api/tests-env/project1/TelosysTools/templates/basic-templates-samples-T300/templates.cfg" ) ;
		
		print("" );
		if ( ! yesOption ) {
			print("Do you want to launch the generation [y/n] ? " );
			Scanner scanner = new Scanner(System.in);
			String choice = scanner.next();
			if ( choice.startsWith("y") || choice.startsWith("Y") ) {
				launchGeneration(launcher);
			}
			else {
				print("OK, bye." );
			}		
		}
		else {
			launchGeneration(launcher);
		}
	}
	
//	private static void testFileExists(String s ) {
//		File f = new File(s);
//		print("TEST : file : " + f.getAbsolutePath());
//		print("TEST : file exists ? : " + f.exists() );
//	}
	
	private static void checkArgs(String[] args ) {
		print(args.length + " argument(s) :");
		for( String arg : args ) {
			print(" . " + arg );
		}

		if ( args.length < 1 || args.length > 2 ) {
			print("ERROR : invalid arguments !");
			printSyntax();
			System.exit(1);
		}
	}

	private static void printSyntax() {
		print("Syntax : ");
		print(" tl launcher-name    ");
		print(" tl launcher-name -y ");
		print(" -y : Automatic yes to prompts");
		print("      assume 'yes' as answer to all prompts and run non-interactively. ");
	}
	
	private static void print(String s ) {
		System.out.println(s);
	}

	private static GenerationTaskResult launchGeneration(TelosysLauncher launcher) throws TelosysToolsException, GeneratorException {
		print("Lauching generation..." );
		GenerationTaskResult result = launcher.launchGeneration();
		print("End of generation : " );
		print(" " + result.getNumberOfFilesGenerated() + " file(s) generated");
		print(" " + result.getNumberOfResourcesCopied() + " resource(s) copied");
		print(" " + result.getNumberOfGenerationErrors() + " errors(s) ");

		return result;
	}

	private static TelosysLauncher buildLauncher(String workingDirectory, String launcherName ) {
		try {
			return new TelosysLauncher(workingDirectory, launcherName );
			
		} catch (Exception e) {
			print("ERROR : " + e.getMessage());
			//e.printStackTrace();
			System.exit(1);
			return null ;
		}
	}
}
