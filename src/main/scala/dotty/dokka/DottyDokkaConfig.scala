package dotty.dokka

import org.jetbrains.dokka._
import org.jetbrains.dokka.DokkaSourceSetImpl
import java.io.File
import java.util.{ List => JList, Map => JMap}
import collection.JavaConverters._

case class DottyDokkaConfig(docConfiguration: DocConfiguration) extends DokkaConfiguration:
  override def getOutputDir: File = docConfiguration.args.output
  override def getCacheRoot: File = null
  override def getOfflineMode: Boolean = false
  override def getFailOnWarning: Boolean = false
  override def getSourceSets: JList[DokkaConfiguration.DokkaSourceSet] = List(mkSourceSet).asJava
  override def getModules: JList[DokkaConfiguration.DokkaModuleDescription] = List().asJava
  override def getPluginsClasspath: JList[File] = Nil.asJava

  override def getPluginsConfiguration: JMap[String, String] = Map(
    "dotty.dokka.DottyDokkaPlugin" -> "dottydoc",
    "ExternalDocsTooKey" -> docConfiguration.args.docsRoot.orNull
    ).asJava

  def mkSourceSet: DokkaConfiguration.DokkaSourceSet = 
    val sourceLinks:Set[SourceLinkDefinitionImpl] = docConfiguration.args.sourceLinks.map(SourceLinkDefinitionImpl.Companion.parseSourceLinkDefinition(_)).toSet
    new DokkaSourceSetImpl(
      /*moduleDisplayName=*/ docConfiguration.args.name,
      /*displayName=*/ docConfiguration.args.name,
      /*sourceSetID=*/ new DokkaSourceSetID(docConfiguration.args.name, "main"),
      /*classpath=*/ Nil.asJava,
      /*sourceRoots=*/ Set().asJava,
      /*dependentSourceSets=*/ Set().asJava,
      /*samples=*/ Set().asJava,
      /*includes=*/ Set().asJava,
      /*includeNonPublic=*/ true,
      /*reportUndocumented=*/ false, /* changed because of exception in reportUndocumentedTransformer - there's 'when' which doesnt match because it contains only KotlinVisbility cases */
      /*skipEmptyPackages=*/ true,
      /*skipDeprecated=*/ true,
      /*jdkVersion=*/ 8,
      /*sourceLinks=*/ sourceLinks.asJava,
      /*perPackageOptions=*/ Nil.asJava,
      /*externalDocumentationLinks=*/ Set().asJava,
      /*languageVersion=*/ null,
      /*apiVersion=*/ null,
      /*noStdlibLink=*/ true,
      /*noJdkLink=*/  true,
      /*suppressedFiles=*/  Set().asJava,
      /*suppressedFiles=*/  Platform.jvm
    ).asInstanceOf[DokkaConfiguration.DokkaSourceSet] // Why I do need to cast here? Kotlin magic?


object FakeDottyDokkaModule extends DokkaConfiguration.DokkaModuleDescription:
  override def getDocFile(): File = new File("docs.doc")
  override def getName() = "main"
  override def getPath() = new File(".")
