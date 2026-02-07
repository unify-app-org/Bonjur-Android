
package com.bonjur.designSystem.commonModel

import androidx.compose.ui.graphics.Color
import com.bonjur.designSystem.ui.theme.colors.Palette
import java.util.UUID

object AppUIEntities {
    
    // MARK: - Member Model
    
    data class Member(
        val uuid: UUID = UUID.randomUUID(),
        val id: Int,
        val profileImage: String?
    )
    
    // MARK: - Tags
    
    data class Tags(
        val uuid: UUID = UUID.randomUUID(),
        val id: Int,
        val type: String,
        val title: String
    )
    
    // MARK: - Access Type
    
    enum class AccessType {
        PUBLIC,
        PRIVATE
    }
    
    // MARK: - Request Type
    
    enum class RequestType {
        JOINED,
        REJECTED,
        PENDING,
        NONE
    }
    
    // MARK: - Background Color Type
    
    sealed class BackgroundType {
        /// green
        object Primary : BackgroundType()
        /// blue
        object Secondary : BackgroundType()
        /// purple
        object Tertiary : BackgroundType()
        data class CustomColor(val colorType: ColorType) : BackgroundType()
        
        val bgColor: Color
            get() = when (this) {
                is Primary -> Palette.primary
                is Secondary -> Palette.cardBgSecondary
                is Tertiary -> Palette.cardBgTertiary
                is CustomColor -> when (colorType) {
                    is ColorType.Orange -> Palette.cardBgOrange
                    is ColorType.Red -> Palette.cardBgRed
                    is ColorType.Pink -> Palette.cardBgPink
                    is ColorType.Custom -> colorType.color
                }
            }
        
        val foregroundColor: Color
            get() = when (this) {
                is Primary, is Tertiary -> Palette.blackHigh
                is Secondary -> Palette.whiteHigh
                is CustomColor -> when (colorType) {
                    is ColorType.Red -> Palette.whiteHigh
                    is ColorType.Pink, is ColorType.Orange -> Palette.blackHigh
                    is ColorType.Custom -> colorType.foregroundColor
                }
            }
        
        val arrowTint: Color
            get() = when (this) {
                is Primary -> Palette.whiteHigh
                is Tertiary, is Secondary -> Palette.blackHigh
                is CustomColor -> when (colorType) {
                    is ColorType.Pink, is ColorType.Red, is ColorType.Orange -> Palette.blackHigh
                    is ColorType.Custom -> colorType.arrowTint
                }
            }
        
        val arrowBgColor: Color
            get() = when (this) {
                is Primary -> Palette.cardBgSecondary
                is Tertiary, is Secondary -> Palette.primary
                is CustomColor -> when (colorType) {
                    is ColorType.Pink, is ColorType.Red, is ColorType.Orange -> Palette.whiteHigh
                    is ColorType.Custom -> colorType.arrowBgColor
                }
            }
    }
    
    sealed class ColorType {
        object Orange : ColorType()
        object Red : ColorType()
        object Pink : ColorType()
        data class Custom(
            val color: Color,
            val foregroundColor: Color,
            val arrowBgColor: Color = Color.White,
            val arrowTint: Color = Palette.blackHigh
        ) : ColorType()
    }
    
    // MARK: - Activity Types
    
    enum class ActivityType {
        COMMUNITY,
        EVENTS,
        CLUBS,
        HANG_OUTS
    }

    enum class UserActivityRole(val title: String) {
        MEMBER("Members"),
        PRESIDENT("President"),
        VISE_PRESIDENT("Vise president"),
        EVENT_CREATOR("Event creators"),
        NOT_JOINED("")
    }

}