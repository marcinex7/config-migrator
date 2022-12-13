import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import pl.jalokim.propertiestojson.util.PropertiesToJsonConverter

import java.nio.file.Path

def configPath = args[0]

def configGroovy = Path.of(configPath).toFile()
if (configPath.isBlank() || !configGroovy.exists()) {
    println "Incorrect config file path!"
    println "Usage: './gradlew migrate -Pfile=<ABSOLUTE_PATH_TO_CONFIG_FILE>"
    System.exit(0)
}

def config = new ConfigSlurper("production").parse(configGroovy.text).toProperties().sort(Comparator.comparing {it.toString().toLowerCase()})

def json = new PropertiesToJsonConverter().convertToJson(config)
def jsonNode = new ObjectMapper().readTree(json)
def yaml = new YAMLMapper().writeValueAsString(jsonNode)

def fullPath = FilenameUtils.getFullPath(configPath)
def baseName = FilenameUtils.getBaseName(configPath)

Path.of(fullPath, "${baseName}.json").toFile().write(json)
Path.of(fullPath, "${baseName}.yaml").toFile().write(yaml)
