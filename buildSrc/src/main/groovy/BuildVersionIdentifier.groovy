import org.gradle.api.GradleException

/**
 *  Provides build version identifiers based on date, overrides and the nth build on date.
 */
class BuildVersionIdentifier {
    static def getLatestDate() {
        return new Date().format('yyMMdd')
    }

    static int getGeneratedVersionCode(File versionCodeFile, String versionCodeSuffixOverride = "") {
        if (versionCodeFile.canRead()) {
            def versionCodeTextOld = versionCodeFile.text
            println sprintf("Previous version-code: %s", versionCodeTextOld)

            def latestDate = getLatestDate()

            if (versionCodeTextOld.isEmpty() || !versionCodeSuffixOverride.isBlank()) {
                def latestVersionCodeSuffix = Integer.parseInt(versionCodeSuffixOverride == null ? 0 : versionCodeSuffixOverride)
                return (Integer.parseInt(latestDate) * 100) + latestVersionCodeSuffix
            }

            def lastDate = versionCodeTextOld.substring(
                    0,
                    versionCodeTextOld.length() - 2
            )

            // consists only 2 digits
            def lastVersionCodeSuffix = Integer.parseInt(versionCodeTextOld.substring(
                    versionCodeTextOld.length() - 2
            ))

            def latestVersionCodeSuffix = lastVersionCodeSuffix + 1

            if (lastDate != latestDate || latestVersionCodeSuffix > 99) {
                latestVersionCodeSuffix = 0
            }

            def latestDateInt = Integer.parseInt(latestDate)

            return (latestDateInt * 100) + latestVersionCodeSuffix
        } else {
            throw GradleException("Could not read in '../version-code'")
        }
    }

    /**
     * Creates version code based on date.
     * Change the [versionCodeOverride] value to allow different build code for the same day.
     * For example: ...00, ...01, ...02, etc.
     */
    static int getVersionCode(File versionCodeFile, String versionCodeOverride = "") {
        def value = getGeneratedVersionCode(versionCodeFile, versionCodeOverride)
        println sprintf("New version-code: " + value.toString())
        if (versionCodeFile.canWrite()) {
            versionCodeFile.write(value.toString())
        } else {
            println("Could not write in '../version-code'")
        }
        return value
    }

    static String getVersionName(File versionNameFile) {
        if (versionNameFile.canRead()) {
            def versionNameText = versionNameFile.text

            println sprintf("Current version name: %s", versionNameText)
            println sprintf("Hint: Update %s for a newer version name", versionNameFile.path)

            if (versionNameText != null) return versionNameText
            return '0.1'
        } else {
            throw new GradleException("Could not read in '../version'")
        }
    }
}
