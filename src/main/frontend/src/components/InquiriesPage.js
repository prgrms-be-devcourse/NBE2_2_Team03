import React from 'react';

const InquiriesPage = ({ inquiries }) => {
    return (
        <div>
            <h2 className="text-2xl font-semibold mb-4">1:1 문의</h2>
            <ul>
                {inquiries.map((inquiry) => (
                    <li key={inquiry.id} className="mb-4 p-4 bg-white rounded shadow">
                        <h3 className="font-semibold">{inquiry.question}</h3>
                        <p>답변: {inquiry.answer || "답변 대기 중"}</p>
                        <p>날짜: {inquiry.date}</p>
                        <p>상태: {inquiry.status}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default InquiriesPage;
