import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import pl.jalokim.propertiestojson.util.PropertiesToJsonConverter

def configGroovy = new File("/Users/e-mnps/Projects/config-migrator/src/main/resources", "config.groovy")

def config = new ConfigSlurper("production").parse(configGroovy.text).toProperties().sort(Comparator.comparing {it.toString().toLowerCase()})

def json = new PropertiesToJsonConverter().convertToJson(config)

def jsonNode = new ObjectMapper().readTree(json)

def yaml = new YAMLMapper().writeValueAsString(jsonNode)

new File("/Users/e-mnps/Projects/config-migrator/src/main/resources", "config.json").write(json)
new File("/Users/e-mnps/Projects/config-migrator/src/main/resources", "config.yaml").write(yaml)

println yaml
