package com.project01.skillineserver.enums;

public enum EmailType {
    WELCOME,                    // Email chào mừng user mới
    COURSE_ENROLLED,            // Đăng ký khóa học thành công
    COURSE_REMINDER,            // Nhắc nhở học bài
    CERTIFICATE_ISSUED,         // Cấp chứng chỉ
    PAYMENT_SUCCESS,            // Thanh toán thành công
    LIVE_STREAM_REMINDER,       // Nhắc lịch live-stream
    PASSWORD_RESET,             // Đặt lại mật khẩu
    ASSIGNMENT_DEADLINE,        // Hết hạn nộp bài
    TEACHER_APPLICATION,        // Đăng ký làm giáo viên
}
