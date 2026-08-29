package com.example.fundforgoals.supabase.repository

data class MemberContributionData(
    val contributorId: Int,
    val projectId: Int,
    val projectTitle: String,
    val organisationName: String,
    val fundAmount: Double,
    val isOngoing: Boolean,
    val hasECertificate: Boolean
)

class MemberContributionRepository(
    private val contributorRepository: ContributorRepository = ContributorRepository(),
    private val projectRepository: ProjectRepository = ProjectRepository(),
    private val userRepository: UserRepository = UserRepository()
) {

    suspend fun getContributionsForUser(userId: Int): List<MemberContributionData> {
        val contributions = contributorRepository.getContributorsByUserId(userId)
        if (contributions.isEmpty()) return emptyList()

        val projectIds = contributions.map { it.project }.distinct()
        val projects = projectRepository.getProjectsByIds(projectIds)
        val projectById = projects.associateBy { it.id }

        val creatorIds = projects.map { it.createdBy }.distinct()
        val creatorNameById = userRepository.getUsersByIds(creatorIds)
            .associateBy({ it.id }, { it.name })

        return contributions.mapNotNull { contributor ->
            val project = projectById[contributor.project] ?: return@mapNotNull null

            MemberContributionData(
                contributorId = contributor.id ?: -1,
                projectId = project.id ?: -1,
                projectTitle = project.title,
                organisationName = creatorNameById[project.createdBy] ?: "Unknown",
                fundAmount = contributor.fundAmount,
                isOngoing = project.status.equals("Ongoing", ignoreCase = true),
                hasECertificate = project.hasCert
            )
        }
    }
}