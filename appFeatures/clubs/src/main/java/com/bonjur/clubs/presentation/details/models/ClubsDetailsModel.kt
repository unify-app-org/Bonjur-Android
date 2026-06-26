package com.bonjur.clubs.presentation.model

import com.bonjur.appfoundation.*
import com.bonjur.clubs.domain.models.ClubsDetails
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.segmentView.SegmentedPickerOption
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.member.model.MemberCellModel

// MARK: - ClubDetails input
data class ClubDetailsInputData(
    val clubId: Int
)

// MARK: - Side effects
sealed class ClubDetailsSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : ClubDetailsSideEffect()
}

// MARK: - View State
data class ClubDetailsViewState(
    val uiModel: ClubsDetails.UIModel? = null,
    val selectedSegment: SegmentTypes = SegmentTypes.ABOUT,
    val membersData: GroupedMembersData? = null,
    val currentUserId: String? = null
) : FeatureState {

    private val role: AppUIEntities.UserActivityRole?
        get() = uiModel?.userActivityType

    /** Owner/VP may edit the club. Mirrors iOS `isEditable`. */
    val isEditable: Boolean
        get() = role == AppUIEntities.UserActivityRole.VISE_PRESIDENT ||
                role == AppUIEntities.UserActivityRole.PRESIDENT

    /** Any joined non-member role may create events. Mirrors iOS `canCreateEvent`. */
    val canCreateEvent: Boolean
        get() = role != null &&
                role != AppUIEntities.UserActivityRole.MEMBER &&
                role != AppUIEntities.UserActivityRole.NOT_JOINED

    /** A not-yet-joined viewer sees the join/request button. */
    val showJoinButton: Boolean
        get() = role == null || role == AppUIEntities.UserActivityRole.NOT_JOINED

    val isPrivate: Boolean
        get() = uiModel?.accessType == AppUIEntities.AccessType.PRIVATE

    /** Verified club → badge instead of the request button. Mirrors iOS `isVerified`. */
    val isVerified: Boolean
        get() = uiModel?.clubStatus?.isVerified == true

    /** Request-verify button: club admins only, and only while the club isn't verified. */
    val showVerifyButton: Boolean
        get() = isEditable && !isVerified

    /** A pending verification request keeps the button visible but disabled. */
    val verifyButtonDisabled: Boolean
        get() = uiModel?.clubStatus == AppUIEntities.ClubStatus.PENDING

    enum class SegmentTypes(
        override val title: String
    ) : SegmentedPickerOption {

        ABOUT("About"),
        EVENTS("Events"),
        MEMBERS("Members");

        override val id: String get() = name

        companion object {
            fun fromIndex(index: Int): SegmentTypes {
                return when (index) {
                    0 -> SegmentTypes.ABOUT
                    1 -> SegmentTypes.EVENTS
                    2 -> SegmentTypes.MEMBERS
                    else -> SegmentTypes.ABOUT
                }
            }
        }

        fun toIndex(): Int {
            return when (this) {
                SegmentTypes.ABOUT -> 0
                SegmentTypes.EVENTS -> 1
                SegmentTypes.MEMBERS -> 2
            }
        }
    }
}

// MARK: - Feature Action
sealed class ClubDetailsAction : FeatureAction {
    object FetchData : ClubDetailsAction()
    object BackTapped : ClubDetailsAction()
    data class SegmentChanged(val segment: ClubDetailsViewState.SegmentTypes) : ClubDetailsAction()
    object EditTapped : ClubDetailsAction()
    object JoinClubTapped : ClubDetailsAction()
    object ExitTapped : ClubDetailsAction()
    object SeeAllMembersTapped : ClubDetailsAction()
    data class MemberTapped(val member: MemberCellModel) : ClubDetailsAction()
    data class AssignRole(
        val userId: String,
        val role: AppUIEntities.UserActivityRole
    ) : ClubDetailsAction()
    object RequestVerificationTapped : ClubDetailsAction()
}