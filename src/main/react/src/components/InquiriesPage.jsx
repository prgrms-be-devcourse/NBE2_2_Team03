import React from 'react';

const InquiriesPage = ({ inquiries }) => {
    return (
        <div className="p-6">
            <h2 className="text-2xl font-semibold mb-4 text-gray-800">1:1 문의</h2> {/* 제목 텍스트 색상 변경 */}
            <ul>
                {inquiries.map((inquiry) => (
                    <li key={inquiry.id} className="mb-4 p-4 bg-white rounded shadow">
                        <h3 className="font-semibold text-gray-800">{inquiry.question}</h3> {/* 질문 텍스트 색상 변경 */}
                        <p className="text-gray-700">답변: {inquiry.answer || "답변 대기 중"}</p> {/* 답변 텍스트 색상 변경 */}
                        <p className="text-gray-600">날짜: {inquiry.date}</p> {/* 날짜 텍스트 색상 변경 */}
                        <p className="text-gray-600">상태: {inquiry.status}</p> {/* 상태 텍스트 색상 변경 */}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default InquiriesPage;
