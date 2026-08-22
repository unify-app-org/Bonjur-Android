package com.bonjur.notification.presentation.verification.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000$\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\u001a4\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u00072\u0014\u0010\b\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\n\u0012\u0004\u0012\u00020\u00030\tH\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"NOTE_LIMIT", "", "RejectVerificationSheet", "", "item", "Lcom/bonjur/notification/domain/models/VerificationItem;", "onDismiss", "Lkotlin/Function0;", "onReject", "Lkotlin/Function1;", "", "notification_debug"})
public final class RejectVerificationSheetKt {
    private static final int NOTE_LIMIT = 300;
    
    /**
     * Confirmation sheet for rejecting a club's verification request. Compose port
     * of iOS `RejectVerificationSheet` — replaces the old confirm alert and doubles
     * as the input for the optional note (`rejectionReason`) the club organiser reads.
     */
    @androidx.compose.runtime.Composable()
    public static final void RejectVerificationSheet(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.models.VerificationItem item, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onReject) {
    }
}