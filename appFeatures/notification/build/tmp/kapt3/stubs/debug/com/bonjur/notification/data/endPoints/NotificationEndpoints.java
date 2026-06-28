package com.bonjur.notification.data.endPoints;

/**
 * Notification-service endpoints. Mirrors iOS `NotificationEndPoint`.
 * The notification feed itself is still mock; only the join-request +
 * verification flows are live.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u00002\u00020\u0001:\u0006\u0003\u0004\u0005\u0006\u0007\bB\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0006\t\n\u000b\f\r\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/bonjur/notification/data/endPoints/NotificationEndpoints;", "Lcom/bonjur/network/APIClient/AppEndpoint;", "()V", "ClubJoinRequests", "ClubPending", "HangoutJoinRequests", "SetClubRequestStatus", "SetClubVerification", "SetHangoutRequestStatus", "Lcom/bonjur/notification/data/endPoints/NotificationEndpoints$ClubJoinRequests;", "Lcom/bonjur/notification/data/endPoints/NotificationEndpoints$ClubPending;", "Lcom/bonjur/notification/data/endPoints/NotificationEndpoints$HangoutJoinRequests;", "Lcom/bonjur/notification/data/endPoints/NotificationEndpoints$SetClubRequestStatus;", "Lcom/bonjur/notification/data/endPoints/NotificationEndpoints$SetClubVerification;", "Lcom/bonjur/notification/data/endPoints/NotificationEndpoints$SetHangoutRequestStatus;", "notification_debug"})
public abstract class NotificationEndpoints implements com.bonjur.network.APIClient.AppEndpoint {
    
    private NotificationEndpoints() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getBody() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.util.Map<java.lang.String, java.lang.String> getHeaders() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public com.bonjur.network.APIClient.MultipartPayload getMultipart() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.util.Map<java.lang.String, java.lang.String> getQueryParameters() {
        return null;
    }
    
    @java.lang.Override()
    public boolean getRequiresAuth() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0019\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u0015\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u001f\u0010\u0012\u001a\u00020\u00002\u0014\b\u0002\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\u0004H\u00d6\u0001R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u0004X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001d\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR \u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000e\u00a8\u0006\u001a"}, d2 = {"Lcom/bonjur/notification/data/endPoints/NotificationEndpoints$ClubJoinRequests;", "Lcom/bonjur/notification/data/endPoints/NotificationEndpoints;", "query", "", "", "(Ljava/util/Map;)V", "method", "Lcom/bonjur/network/APIClient/NetworkMethod;", "getMethod", "()Lcom/bonjur/network/APIClient/NetworkMethod;", "path", "getPath", "()Ljava/lang/String;", "getQuery", "()Ljava/util/Map;", "queryParameters", "getQueryParameters", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "notification_debug"})
    public static final class ClubJoinRequests extends com.bonjur.notification.data.endPoints.NotificationEndpoints {
        @org.jetbrains.annotations.NotNull()
        private final java.util.Map<java.lang.String, java.lang.String> query = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String path = "api/cs/v1/clubs/join-requests";
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.network.APIClient.NetworkMethod method = com.bonjur.network.APIClient.NetworkMethod.GET;
        @org.jetbrains.annotations.NotNull()
        private final java.util.Map<java.lang.String, java.lang.String> queryParameters = null;
        
        public ClubJoinRequests(@org.jetbrains.annotations.NotNull()
        java.util.Map<java.lang.String, java.lang.String> query) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.Map<java.lang.String, java.lang.String> getQuery() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String getPath() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.bonjur.network.APIClient.NetworkMethod getMethod() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.util.Map<java.lang.String, java.lang.String> getQueryParameters() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.Map<java.lang.String, java.lang.String> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.data.endPoints.NotificationEndpoints.ClubJoinRequests copy(@org.jetbrains.annotations.NotNull()
        java.util.Map<java.lang.String, java.lang.String> query) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    /**
     * Admin: clubs awaiting verification.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0019\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u0015\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u001f\u0010\u0012\u001a\u00020\u00002\u0014\b\u0002\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\u0004H\u00d6\u0001R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u0004X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001d\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR \u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000e\u00a8\u0006\u001a"}, d2 = {"Lcom/bonjur/notification/data/endPoints/NotificationEndpoints$ClubPending;", "Lcom/bonjur/notification/data/endPoints/NotificationEndpoints;", "query", "", "", "(Ljava/util/Map;)V", "method", "Lcom/bonjur/network/APIClient/NetworkMethod;", "getMethod", "()Lcom/bonjur/network/APIClient/NetworkMethod;", "path", "getPath", "()Ljava/lang/String;", "getQuery", "()Ljava/util/Map;", "queryParameters", "getQueryParameters", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "notification_debug"})
    public static final class ClubPending extends com.bonjur.notification.data.endPoints.NotificationEndpoints {
        @org.jetbrains.annotations.NotNull()
        private final java.util.Map<java.lang.String, java.lang.String> query = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String path = "api/cs/v1/clubs/pending";
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.network.APIClient.NetworkMethod method = com.bonjur.network.APIClient.NetworkMethod.GET;
        @org.jetbrains.annotations.NotNull()
        private final java.util.Map<java.lang.String, java.lang.String> queryParameters = null;
        
        public ClubPending(@org.jetbrains.annotations.NotNull()
        java.util.Map<java.lang.String, java.lang.String> query) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.Map<java.lang.String, java.lang.String> getQuery() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String getPath() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.bonjur.network.APIClient.NetworkMethod getMethod() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.util.Map<java.lang.String, java.lang.String> getQueryParameters() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.Map<java.lang.String, java.lang.String> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.data.endPoints.NotificationEndpoints.ClubPending copy(@org.jetbrains.annotations.NotNull()
        java.util.Map<java.lang.String, java.lang.String> query) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0019\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u0015\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u001f\u0010\u0012\u001a\u00020\u00002\u0014\b\u0002\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\u0004H\u00d6\u0001R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u0004X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001d\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR \u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000e\u00a8\u0006\u001a"}, d2 = {"Lcom/bonjur/notification/data/endPoints/NotificationEndpoints$HangoutJoinRequests;", "Lcom/bonjur/notification/data/endPoints/NotificationEndpoints;", "query", "", "", "(Ljava/util/Map;)V", "method", "Lcom/bonjur/network/APIClient/NetworkMethod;", "getMethod", "()Lcom/bonjur/network/APIClient/NetworkMethod;", "path", "getPath", "()Ljava/lang/String;", "getQuery", "()Ljava/util/Map;", "queryParameters", "getQueryParameters", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "notification_debug"})
    public static final class HangoutJoinRequests extends com.bonjur.notification.data.endPoints.NotificationEndpoints {
        @org.jetbrains.annotations.NotNull()
        private final java.util.Map<java.lang.String, java.lang.String> query = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String path = "api/hs/v1/hangouts/join-requests";
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.network.APIClient.NetworkMethod method = com.bonjur.network.APIClient.NetworkMethod.GET;
        @org.jetbrains.annotations.NotNull()
        private final java.util.Map<java.lang.String, java.lang.String> queryParameters = null;
        
        public HangoutJoinRequests(@org.jetbrains.annotations.NotNull()
        java.util.Map<java.lang.String, java.lang.String> query) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.Map<java.lang.String, java.lang.String> getQuery() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String getPath() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.bonjur.network.APIClient.NetworkMethod getMethod() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.util.Map<java.lang.String, java.lang.String> getQueryParameters() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.Map<java.lang.String, java.lang.String> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.data.endPoints.NotificationEndpoints.HangoutJoinRequests copy(@org.jetbrains.annotations.NotNull()
        java.util.Map<java.lang.String, java.lang.String> query) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    /**
     * Accept/reject a club join request. status ∈ {ACCEPT, REJECT}.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\rH\u00d6\u0001R\u0014\u0010\u0005\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\u00020\tX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\rX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0007\u00a8\u0006\u001a"}, d2 = {"Lcom/bonjur/notification/data/endPoints/NotificationEndpoints$SetClubRequestStatus;", "Lcom/bonjur/notification/data/endPoints/NotificationEndpoints;", "request", "Lcom/bonjur/notification/data/DTOs/ClubStatusRequest;", "(Lcom/bonjur/notification/data/DTOs/ClubStatusRequest;)V", "body", "getBody", "()Lcom/bonjur/notification/data/DTOs/ClubStatusRequest;", "method", "Lcom/bonjur/network/APIClient/NetworkMethod;", "getMethod", "()Lcom/bonjur/network/APIClient/NetworkMethod;", "path", "", "getPath", "()Ljava/lang/String;", "getRequest", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "notification_debug"})
    public static final class SetClubRequestStatus extends com.bonjur.notification.data.endPoints.NotificationEndpoints {
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.notification.data.DTOs.ClubStatusRequest request = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String path = "api/cs/v1/clubs/join-requests/status";
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.network.APIClient.NetworkMethod method = com.bonjur.network.APIClient.NetworkMethod.POST;
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.notification.data.DTOs.ClubStatusRequest body = null;
        
        public SetClubRequestStatus(@org.jetbrains.annotations.NotNull()
        com.bonjur.notification.data.DTOs.ClubStatusRequest request) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.data.DTOs.ClubStatusRequest getRequest() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String getPath() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.bonjur.network.APIClient.NetworkMethod getMethod() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.bonjur.notification.data.DTOs.ClubStatusRequest getBody() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.data.DTOs.ClubStatusRequest component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.data.endPoints.NotificationEndpoints.SetClubRequestStatus copy(@org.jetbrains.annotations.NotNull()
        com.bonjur.notification.data.DTOs.ClubStatusRequest request) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    /**
     * Admin: approve/reject a club's verification. status ∈ {ACCEPT, REJECT}.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\rH\u00d6\u0001R\u0014\u0010\u0005\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\u00020\tX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\rX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0007\u00a8\u0006\u001a"}, d2 = {"Lcom/bonjur/notification/data/endPoints/NotificationEndpoints$SetClubVerification;", "Lcom/bonjur/notification/data/endPoints/NotificationEndpoints;", "request", "Lcom/bonjur/notification/data/DTOs/ClubVerificationRequest;", "(Lcom/bonjur/notification/data/DTOs/ClubVerificationRequest;)V", "body", "getBody", "()Lcom/bonjur/notification/data/DTOs/ClubVerificationRequest;", "method", "Lcom/bonjur/network/APIClient/NetworkMethod;", "getMethod", "()Lcom/bonjur/network/APIClient/NetworkMethod;", "path", "", "getPath", "()Ljava/lang/String;", "getRequest", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "notification_debug"})
    public static final class SetClubVerification extends com.bonjur.notification.data.endPoints.NotificationEndpoints {
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.notification.data.DTOs.ClubVerificationRequest request = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String path = "api/cs/v1/clubs/status";
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.network.APIClient.NetworkMethod method = com.bonjur.network.APIClient.NetworkMethod.POST;
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.notification.data.DTOs.ClubVerificationRequest body = null;
        
        public SetClubVerification(@org.jetbrains.annotations.NotNull()
        com.bonjur.notification.data.DTOs.ClubVerificationRequest request) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.data.DTOs.ClubVerificationRequest getRequest() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String getPath() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.bonjur.network.APIClient.NetworkMethod getMethod() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.bonjur.notification.data.DTOs.ClubVerificationRequest getBody() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.data.DTOs.ClubVerificationRequest component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.data.endPoints.NotificationEndpoints.SetClubVerification copy(@org.jetbrains.annotations.NotNull()
        com.bonjur.notification.data.DTOs.ClubVerificationRequest request) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    /**
     * Accept/reject a hangout join request. status ∈ {ACCEPTED, REJECTED}.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001J\t\u0010\u001c\u001a\u00020\u0003H\u00d6\u0001R\u0014\u0010\u0007\u001a\u00020\u0005X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\rX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\t\u00a8\u0006\u001d"}, d2 = {"Lcom/bonjur/notification/data/endPoints/NotificationEndpoints$SetHangoutRequestStatus;", "Lcom/bonjur/notification/data/endPoints/NotificationEndpoints;", "hangoutId", "", "request", "Lcom/bonjur/notification/data/DTOs/HangoutStatusRequest;", "(Ljava/lang/String;Lcom/bonjur/notification/data/DTOs/HangoutStatusRequest;)V", "body", "getBody", "()Lcom/bonjur/notification/data/DTOs/HangoutStatusRequest;", "getHangoutId", "()Ljava/lang/String;", "method", "Lcom/bonjur/network/APIClient/NetworkMethod;", "getMethod", "()Lcom/bonjur/network/APIClient/NetworkMethod;", "path", "getPath", "getRequest", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "notification_debug"})
    public static final class SetHangoutRequestStatus extends com.bonjur.notification.data.endPoints.NotificationEndpoints {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String hangoutId = null;
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.notification.data.DTOs.HangoutStatusRequest request = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String path = null;
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.network.APIClient.NetworkMethod method = com.bonjur.network.APIClient.NetworkMethod.POST;
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.notification.data.DTOs.HangoutStatusRequest body = null;
        
        public SetHangoutRequestStatus(@org.jetbrains.annotations.NotNull()
        java.lang.String hangoutId, @org.jetbrains.annotations.NotNull()
        com.bonjur.notification.data.DTOs.HangoutStatusRequest request) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getHangoutId() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.data.DTOs.HangoutStatusRequest getRequest() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String getPath() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.bonjur.network.APIClient.NetworkMethod getMethod() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.bonjur.notification.data.DTOs.HangoutStatusRequest getBody() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.data.DTOs.HangoutStatusRequest component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.data.endPoints.NotificationEndpoints.SetHangoutRequestStatus copy(@org.jetbrains.annotations.NotNull()
        java.lang.String hangoutId, @org.jetbrains.annotations.NotNull()
        com.bonjur.notification.data.DTOs.HangoutStatusRequest request) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}