package com.bonjur.member.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.member.model.MemberCellModel
import com.bonjur.member.model.MemberListSectionModel

/**
 * Shared member-list components, used by every activity module
 * (clubs / events / hangouts / communities). Compose port of the iOS
 * `MemberListView` / `MemberCellView` / `MemberSectionHeaderView`.
 */

/** Trailing accessory shown on a member row. Mirrors iOS `MemberCellAccessory`. */
sealed interface MemberCellAccessory {
    data object None : MemberCellAccessory
    data object Disclosure : MemberCellAccessory
    data class Checkbox(val isSelected: Boolean) : MemberCellAccessory
    data object OptionsMenu : MemberCellAccessory
}

/**
 * Options accessory that hides the 3-dot on the current user's own row — you can't
 * act on yourself, so no menu. Mirrors iOS `MemberCellViewData.options(from:currentUserId:)`.
 */
fun memberOptionsAccessory(
    member: MemberCellModel,
    currentUserId: String?
): MemberCellAccessory {
    val isSelf = !currentUserId.isNullOrEmpty() && member.id == currentUserId
    return if (isSelf) MemberCellAccessory.None else MemberCellAccessory.OptionsMenu
}

/** A single bordered member card. Mirrors iOS `MemberCellView`. */
@Composable
fun MemberCell(
    member: MemberCellModel,
    accessory: MemberCellAccessory,
    onTap: () -> Unit = {},
    onAccessoryTap: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Palette.white)
            .border(1.dp, Palette.grayTeritary.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
            .let { if (accessory !is MemberCellAccessory.OptionsMenu) it.clickable(onClick = onTap) else it }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // The avatar + text region is the tappable area for the options-menu variant.
            val showsSubtitle = accessory is MemberCellAccessory.OptionsMenu &&
                member.subtitle.isNotBlank()
            Row(
                modifier = Modifier
                    .weight(1f)
                    .let { if (accessory is MemberCellAccessory.OptionsMenu) it.clickable(onClick = onTap) else it },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MemberAvatar(member.avatarUrl)
                Column(
                    verticalArrangement = Arrangement.spacedBy(if (showsSubtitle) 4.dp else 0.dp)
                ) {
                    Text(
                        text = member.name,
                        style = AppTypography.BodyTextMd.bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (showsSubtitle) {
                        Text(
                            text = member.subtitle,
                            style = AppTypography.TextMd.bold,
                            color = Palette.graySecondary,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            MemberAccessoryView(accessory = accessory, onAccessoryTap = onAccessoryTap)
        }
    }
}

@Composable
private fun MemberAvatar(avatarUrl: String?) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Palette.grayQuaternary)
            .border(1.dp, Palette.grayTeritary.copy(alpha = 0.7f), RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        CachedAsyncImage(
            url = avatarUrl,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop,
            placeholder = { MemberAvatarFallback() },
            error = { MemberAvatarFallback() }
        )
    }
}

@Composable
private fun MemberAvatarFallback() {
    Icon(
        painter = Images.Icons.user(),
        contentDescription = null,
        tint = Palette.blackMedium,
        modifier = Modifier.size(20.dp)
    )
}

@Composable
private fun MemberAccessoryView(
    accessory: MemberCellAccessory,
    onAccessoryTap: () -> Unit
) {
    when (accessory) {
        MemberCellAccessory.None -> Unit

        MemberCellAccessory.Disclosure -> Icon(
            painter = Images.Icons.chevronRight(),
            contentDescription = null,
            tint = Palette.graySecondary,
            modifier = Modifier.size(24.dp)
        )

        is MemberCellAccessory.Checkbox -> Icon(
            painter = Images.Icons.chevronRight(),
            contentDescription = null,
            tint = if (accessory.isSelected) Palette.appBlue else Palette.grayTeritary,
            modifier = Modifier.size(18.dp)
        )

        MemberCellAccessory.OptionsMenu -> IconButton(
            onClick = onAccessoryTap,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                painter = Images.Icons.ellipsis02(),
                contentDescription = "Options",
                tint = Palette.graySecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/** Section header: role title + member count. Mirrors iOS `MemberSectionHeaderView`. */
@Composable
fun MemberSectionHeader(
    title: String,
    memberCountText: String?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = AppTypography.HeadingMd.bold,
            color = Palette.black
        )
        Spacer(modifier = Modifier.weight(1f))
        if (memberCountText != null) {
            Text(
                text = memberCountText,
                style = AppTypography.TextL.bold,
                color = Palette.grayPrimary
            )
        }
    }
}

/**
 * Sectioned member list. Renders each role section with a header then bordered rows.
 * When [previewLimit] is set, caps rows across sections (in section order) and shows a
 * "See all" affordance once the total exceeds the limit. Mirrors iOS `MemberListView`.
 */
@Composable
fun MemberListView(
    sections: List<MemberListSectionModel>,
    onRowTap: (MemberCellModel) -> Unit,
    modifier: Modifier = Modifier,
    onOptionsTap: ((MemberCellModel) -> Unit)? = null,
    currentUserId: String? = null,
    showCountText: Boolean = true,
    previewLimit: Int? = null,
    totalCount: Int? = null,
    onSeeAll: (() -> Unit)? = null
) {
    val loadedCount = sections.sumOf { it.members.size }
    val visibleSections = capSections(sections, previewLimit)
    val showsSeeAll = previewLimit != null && onSeeAll != null &&
        (totalCount ?: loadedCount) > previewLimit

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        visibleSections.forEach { section ->
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                MemberSectionHeader(
                    title = section.title,
                    memberCountText = if (showCountText) "${section.memberCount}" else null
                )
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    section.members.forEach { member ->
                        val accessory = if (onOptionsTap != null) {
                            memberOptionsAccessory(member, currentUserId)
                        } else {
                            MemberCellAccessory.Disclosure
                        }
                        MemberCell(
                            member = member,
                            accessory = accessory,
                            onTap = { onRowTap(member) },
                            onAccessoryTap = { onOptionsTap?.invoke(member) }
                        )
                    }
                }
            }
        }

        if (showsSeeAll) {
            SeeAllButton(count = totalCount ?: loadedCount, onClick = { onSeeAll?.invoke() })
        }
    }
}

/** Caps total rows to [limit] across sections, preserving section order and grouping. */
private fun capSections(
    sections: List<MemberListSectionModel>,
    limit: Int?
): List<MemberListSectionModel> {
    if (limit == null) return sections
    var remaining = limit
    val result = mutableListOf<MemberListSectionModel>()
    for (section in sections) {
        if (remaining <= 0) break
        val rows = section.members.take(remaining)
        remaining -= rows.size
        result.add(section.copy(members = rows))
    }
    return result
}

@Composable
private fun SeeAllButton(count: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "See all $count members",
            style = AppTypography.TextL.bold,
            color = Palette.appBlue,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            painter = Images.Icons.chevronRight(),
            contentDescription = null,
            tint = Palette.appBlue,
            modifier = Modifier.size(16.dp)
        )
    }
}
