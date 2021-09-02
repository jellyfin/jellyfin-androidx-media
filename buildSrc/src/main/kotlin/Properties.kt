import org.gradle.api.Project
import java.util.Locale

/**
 * Helper function to retrieve configuration variable values
 */
fun Project.getProperty(name: String): String? {
    // sample.var --> SAMPLE_VAR
    val environmentName = name.toUpperCase(Locale.ROOT).replace(".", "_")
    val value = findProperty(name)?.toString() ?: System.getenv(environmentName) ?: null
    logger.debug("getProperty($name): $environmentName - found=${!value.isNullOrBlank()}")
    return value
}

/**
 * Helper function to create the library version using the `jellyfin.version` property.
 * Uses [getProperty] to retrieve the properties. Defaults to `-SNAPSHOT` if no property is set.
 */
fun Project.createVersion(): String {
    val jellyfinVersion = getProperty("jellyfin.version")?.removePrefix("v")
    return jellyfinVersion ?: "SNAPSHOT"
}
