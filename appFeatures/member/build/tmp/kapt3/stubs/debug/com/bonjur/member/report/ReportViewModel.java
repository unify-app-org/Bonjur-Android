package com.bonjur.member.report;

/**
 * Sends reports through the shared [ReportService] and shows the outcome.
 * Every report sheet (member / club / event / hangout) goes through here, so
 * the snackbars are identical everywhere. Mirrors iOS `ReportSubmitter`.
 *
 * Runs in [viewModelScope], not the sheet's scope: the request must survive the
 * sheet being dismissed while it is in flight.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J*\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00060\fJ*\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u00112\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00060\fJ4\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00102\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00060\fH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/bonjur/member/report/ReportViewModel;", "Landroidx/lifecycle/ViewModel;", "service", "Lcom/bonjur/network/report/ReportService;", "(Lcom/bonjur/network/report/ReportService;)V", "reportActivity", "", "target", "Lcom/bonjur/network/report/ReportTarget;", "reason", "Lcom/bonjur/member/policy/ActivityReportReason;", "onResult", "Lkotlin/Function1;", "", "reportUser", "userId", "", "Lcom/bonjur/member/policy/ReportReason;", "submit", "code", "details", "member_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ReportViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.network.report.ReportService service = null;
    
    @javax.inject.Inject()
    public ReportViewModel(@org.jetbrains.annotations.NotNull()
    com.bonjur.network.report.ReportService service) {
        super();
    }
    
    /**
     * [onResult] gets `true` on success — the sheet closes only then.
     */
    public final void reportUser(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    com.bonjur.member.policy.ReportReason reason, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onResult) {
    }
    
    public final void reportActivity(@org.jetbrains.annotations.NotNull()
    com.bonjur.network.report.ReportTarget target, @org.jetbrains.annotations.NotNull()
    com.bonjur.member.policy.ActivityReportReason reason, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onResult) {
    }
    
    private final void submit(com.bonjur.network.report.ReportTarget target, java.lang.String code, java.lang.String details, kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onResult) {
    }
}