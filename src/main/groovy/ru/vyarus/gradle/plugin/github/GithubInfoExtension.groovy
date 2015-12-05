package ru.vyarus.gradle.plugin.github

import org.gradle.api.Project
import ru.vyarus.gradle.plugin.github.helper.LicenseHelper

/**
 * Github info extension.
 * Repository name is set to project name by default.
 * Required fields are: user and license.
 * <p>
 * licenseUrl will lead to LICENSE or LICENSE.txt file if it exists in project root.
 * <p>
 * Most useful licenses are recognized and licenseName and licenseUrl could be set automatically
 * (see {@link LicenseHelper} for all supported). If license is not recognized, other license properties must be
 * filled manually.
 * <p>
 * Other fields are generated automatically using conventions, but they still may be overridden manually.
 * <p>
 * For example, minimal configuration:
 * <pre>
 *     github {
 *         user 'user'
 *         license 'MIT'
 *     }
 * </pre>
 * Generated fields may be used in other configurations like pom generation or bintray upload.
 * Simply reference required property <code>github.site</code>
 * <p>
 * Special method {@code rawFileUrl} may be used to generate direct links to files on github repository.
 *
 * @author Vyacheslav Rusakov 
 * @since 10.11.2015
 */
class GithubInfoExtension {
    private final LicenseHelper helper

    GithubInfoExtension(Project project) {
        helper = new LicenseHelper(project)
        repository = project.name
    }

    /**
     * Github user (or organization) name. Required field.
     */
    String user

    /**
     * License short name (e.g. MIT). Required field.
     */
    String license

    /**
     * Github repository name. If not specified will be the same as project name.
     */
    String repository

    String licenseName
    String licenseUrl
    String repositoryUrl
    String issues
    String site
    String vcsUrl
    String scmConnection

    /**
     * License name (e.g. The MIT License). Required for some licenses.
     */
    String getLicenseName() {
        licenseName ?: helper.defaultLicenseName(this)
    }

    /**
     * License url. If LICENSE (or LICENSE.txt) file contained in repository root, url will lead to this file,
     * otherwise field must be filled manually for some licenses.
     */
    String getLicenseUrl() {
        licenseUrl ?: helper.defaultLicenseUrl(this)
    }

    /**
     * Repository url (github site link). By default, https://github.com/user/repo
     */
    String getRepositoryUrl() {
        repositoryUrl ?: "https://${baseUrlPart()}"
    }

    /**
     * Issue tracker url. By default, github issues url (https://github.com/user/repo/issues).
     */
    String getIssues() {
        issues ?: "https://${baseUrlPart()}/issues"
    }

    /**
     * Site url. By default, repository url (https://github.com/user/repo)
     */
    @SuppressWarnings('UnnecessaryGetter')
    String getSite() {
        site ?: getRepositoryUrl()
    }

    /**
     * Vcs url. By default, github git url (https://github.com/user/repo.git)
     */
    String getVcsUrl() {
        vcsUrl ?: "https://${baseUrlPart()}.git"
    }

    /**
     * Scm connection url. By default, git scm url (scm:git:git://github.com/user/repo.git)
     */
    String getScmConnection() {
        scmConnection ?: "scm:git:git://${baseUrlPart()}.git"
    }

    /**
     * @param filePath file path from project root
     * @param branch git branch (master by default)
     * @return direct link to file on github
     */
    String rawFileUrl(String filePath, String branch = 'master') {
        "https://raw.githubusercontent.com/$user/$repository/$branch/${filePath.replaceAll('\\\\', '/')}"
    }

    /**
     * @return base repository url part (without protocol: 'github.com/user/repo')
     */
    String baseUrlPart() {
        "github.com/${user}/${repository}"
    }
}