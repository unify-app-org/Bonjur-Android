package com.bonjur.member.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.member.components.MemberListView
import com.bonjur.member.components.MemberOptionsInput
import com.bonjur.member.components.MemberOptionsSheet
import com.bonjur.member.model.MemberCellModel
import com.bonjur.member.model.MemberListSectionModel
import com.bonjur.member.policy.MemberOptionsPolicy

/**
 * In-detail members section shared by every activity (clubs / events / hangouts /
 * communities): capped preview list + "See all" + per-row 3-dot options sheet.
 * Single source of truth — each detail's MembersTab is just a thin wrapper.
 * Role-assign is gated by [MemberOptionsPolicy] (CLUBS/COMMUNITY only); pass
 * [onAssignRole] there, leave it default elsewhere.
 */
@Composable
fun MembersPreview(
    sections: List<MemberListSectionModel>,
    totalCount: Int,
    viewerRole: AppUIEntities.UserActivityRole,
    currentUserId: String?,
    activityType: AppUIEntities.ActivityType,
    onMemberTap: (MemberCellModel) -> Unit,
    onSeeAll: () -> Unit,
    onAssignRole: (userId: String, role: AppUIEntities.UserActivityRole) -> Unit = { _, _ -> },
    previewLimit: Int = 5
) {
    var optionsMember by remember { mutableStateOf<MemberCellModel?>(null) }

    MemberListView(
        sections = sections,
        onRowTap = onMemberTap,
        onOptionsTap = { member -> optionsMember = member },
        currentUserId = currentUserId,
        previewLimit = previewLimit,
        totalCount = totalCount,
        onSeeAll = onSeeAll
    )

    optionsMember?.let { member ->
        val isSelf = member.id == currentUserId
        MemberOptionsSheet(
            input = MemberOptionsInput(
                memberName = member.name,
                currentRole = member.role,
                assignableRoles = MemberOptionsPolicy.assignableRoles(viewerRole),
                showChangeRole = MemberOptionsPolicy.canChangeRole(
                    viewer = viewerRole,
                    activity = activityType,
                    isSelf = isSelf
                ),
                showReport = MemberOptionsPolicy.canReport(isSelf),
                onAssignRole = { role -> onAssignRole(member.id, role) },
                onReport = {
                    AppSnackBar.show(title = "Report submitted", style = AppSnackBar.Style.SUCCESS)
                }
            ),
            onDismiss = { optionsMember = null }
        )
    }
}
