import React, { useState } from 'react';

const ProfileModal = ({ user, onUpdate, setIsProfileModalOpen }) => {
    const [username, setUsername] = useState(user.username);
    const [avatar, setAvatar] = useState(user.avatar);

    const handleUpdate = (e) => {
        e.preventDefault();
        onUpdate({ ...user, username, avatar }); // Pass the updated user data
    };

    return (
        <div className="fixed inset-0 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg shadow-md p-6 w-96">
                <h2 className="text-2xl font-semibold mb-4 text-gray-800">회원 정보 수정</h2> {/* 텍스트 색상 변경 */}
                <form onSubmit={handleUpdate}>
                    <div className="mb-4">
                        <label className="block mb-2 text-gray-800">아이디</label> {/* 텍스트 색상 변경 */}
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="w-full p-2 border border-gray-300 rounded-md bg-white text-black" // 흰색 배경에 검정색 텍스트
                            required
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block mb-2 text-gray-800">아바타 이미지</label> {/* 텍스트 색상 변경 */}
                        <img src={avatar} alt="Avatar" className="w-20 h-20 rounded-full mb-2" />
                        <input
                            type="file"
                            accept="image/*"
                            onChange={(e) => {
                                const file = e.target.files[0];
                                if (file) {
                                    const reader = new FileReader();
                                    reader.onloadend = () => {
                                        setAvatar(reader.result); // Set the new avatar
                                    };
                                    reader.readAsDataURL(file);
                                }
                            }}
                            className="w-full p-2 border border-gray-300 rounded-md bg-white" // 흰색 배경
                        />
                    </div>
                    <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700">저장</button> {/* 버튼 스타일 변경 */}
                </form>
                <button onClick={() => setIsProfileModalOpen(false)} className="mt-4 text-blue-600 hover:underline">취소</button> {/* Close button */}
            </div>
        </div>
    );
};

export default ProfileModal;
