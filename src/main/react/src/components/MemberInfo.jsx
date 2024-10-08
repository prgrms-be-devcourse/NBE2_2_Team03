import React, { useState } from 'react';

const MemberInfoPage = ({ user, setUser }) => {
    const [memberInfo, setMemberInfo] = useState({
        email: user.email || '',
        phone: user.phone || '',
    });
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [file, setFile] = useState(null);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setMemberInfo({ ...memberInfo, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const memberId = user.memberId;

        if (!memberId) {
            console.error("Member ID is undefined");
            setError('회원 ID를 찾을 수 없습니다.');
            return;
        }

        setIsLoading(true);
        setError('');

        const token = localStorage.getItem('accessToken');

        fetch(`http://localhost:8000/api/members/${memberId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                email: memberInfo.email,
                phone: memberInfo.phone,
            }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setUser(prevUser => ({
                    ...prevUser,
                    email: memberInfo.email,
                    phone: memberInfo.phone,
                }));
            })
            .catch(error => {
                console.error('Error updating member info:', error);
                setError('회원 정보 수정에 실패했습니다.');
            })
            .finally(() => {
                setIsLoading(false);
            });
    };

    const handleAvatarChange = (e) => {
        const selectedFile = e.target.files[0];
        if (selectedFile) {
            setFile(selectedFile);
        }
    };

    const uploadAvatar = () => {
        const memberId = user.memberId;
        const token = localStorage.getItem('accessToken');

        const formData = new FormData();
        formData.append('avatarImage', file);

        fetch(`http://localhost:8000/api/members/${memberId}/avatar`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            body: formData,
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setUser(prevUser => ({
                    ...prevUser,
                    avatar: data.data // 서버에서 반환된 아바타 URL
                }));
                setFile(null);
            })
            .catch(error => {
                console.error('Error uploading avatar:', error);
                setError('아바타 이미지 업로드에 실패했습니다.');
            });
    };

    const avatarImagePath = user.avatar;
    const avatarFileName = avatarImagePath ? avatarImagePath.split('/').pop() : null;
    const defaultAvatarUrl = 'http://localhost:8000/images/default-avatar.png';
    // const avatarUrl = avatarImagePath ? `http://localhost:8000/${avatarFileName}` : defaultAvatarUrl;    // 기본 이미지 경로를 찾아오지 못해 수정
    const avatarUrl = avatarImagePath && avatarImagePath.includes('default-avatar.png')
        ? defaultAvatarUrl
        : `http://localhost:8000/${avatarFileName}`;

    return (
        <div className="p-6">
            <h1 className="text-2xl font-semibold mb-4 text-gray-800">회원 정보</h1>
            {error && <p className="text-red-500 mb-4">{error}</p>}
            <div className="bg-white p-6 rounded-lg shadow-md mb-4">
                <h2 className="text-lg font-semibold text-gray-800">사용자 정보</h2>
                <p className="text-gray-700"><strong>사용자 ID:</strong> {user.userId}</p>
                <p className="text-gray-700"><strong>이름:</strong> {user.name}</p>
                <p className="text-gray-700"><strong>이메일:</strong> {memberInfo.email}</p>
                <p className="text-gray-700"><strong>전화번호:</strong> {memberInfo.phone}</p>
                <p className="text-gray-700"><strong>역할:</strong> {user.role}</p>
                <div className="flex items-center mt-4">
                    <a href={avatarUrl} target="_blank" rel="noopener noreferrer">
                        <img
                            src={avatarUrl}
                            alt="User Avatar"
                            className="w-24 h-24 rounded-full border-2 border-gray-300 cursor-pointer"
                        />
                    </a>
                    <div className="ml-4">
                        <p className="text-gray-800">아바타 이미지</p>
                    </div>
                </div>
                <div className="mt-4">
                    <label className="block mb-2 text-gray-800">아바타 이미지 수정하기</label>
                    <input
                        type="file"
                        accept="image/*"
                        onChange={handleAvatarChange}
                        className="w-full p-2 border border-gray-300 rounded-md bg-white"
                    />
                    <button
                        type="button"
                        onClick={uploadAvatar}
                        className="mt-2 w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700"
                        disabled={!file}
                    >
                        아바타 업로드
                    </button>
                </div>
            </div>
            <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md">
                <label className="block mb-2 text-gray-800">이메일:</label>
                <input
                    type="email"
                    name="email"
                    value={memberInfo.email}
                    onChange={handleChange}
                    className="w-full p-2 border border-gray-300 rounded-md bg-white text-black mb-4"
                    required
                />
                <label className="block mb-2 text-gray-800">전화번호:</label>
                <input
                    type="tel"
                    name="phone"
                    value={memberInfo.phone}
                    onChange={handleChange}
                    className="w-full p-2 border border-gray-300 rounded-md bg-white text-black mb-4"
                    required
                />
                <button
                    type="submit"
                    className={`w-full ${isLoading ? 'bg-gray-400' : 'bg-blue-600'} text-white py-2 rounded-md hover:bg-blue-700`}
                    disabled={isLoading}
                >
                    {isLoading ? '수정 중...' : '수정하기'}
                </button>
            </form>
        </div>
    );
};

export default MemberInfoPage;
