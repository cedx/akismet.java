/** Runs the test suite. **/
function main() {
	Sys.command("lix Build --debug");

	final pattern = "io/belin/akismet/*.java";
	Tools.setClassPath();
	Sys.command('javac -d bin -g -Xlint:all,-processing test/$pattern');

	final exitCode = Sys.command("java org.junit.platform.console.ConsoleLauncher --select-package=io.belin.akismet");
	if (exitCode != 0) Sys.exit(exitCode);
}
