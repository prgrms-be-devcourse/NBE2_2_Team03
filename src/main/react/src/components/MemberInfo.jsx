import React, { useState } from 'react';

const MemberInfoPage = ({ user, setUser }) => {
    const [memberInfo, setMemberInfo] = useState({
        email: user.email || '',
        phone: user.phone || '',
    });
    const [error, setError] = useState(''); // 에러 상태 추가
    const [isLoading, setIsLoading] = useState(false); // 로딩 상태 추가
    const [file, setFile] = useState(null); // 파일 상태 추가

    const handleChange = (e) => {
        const { name, value } = e.target;
        setMemberInfo({ ...memberInfo, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const memberId = user.memberId; // user 객체에서 memberId 가져오기

        if (!memberId) {
            console.error("Member ID is undefined");
            setError('회원 ID를 찾을 수 없습니다.');
            return;
        }

        // 로딩 상태 시작
        setIsLoading(true);
        setError('');

        // JWT 토큰 가져오기
        const token = localStorage.getItem('accessToken');
        console.log("JWT Token:", token);
        console.log("Member ID:", memberId);

        // 회원 정보 수정 API 호출
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
                console.log('Response status:', response.status); // 상태 코드 로그
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                // 수정 성공 후 사용자 정보 업데이트
                console.log('Member info updated:', data);
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
                // 로딩 상태 종료
                setIsLoading(false);
            });
    };

    const handleAvatarChange = (e) => {
        const selectedFile = e.target.files[0];
        if (selectedFile) {
            setFile(selectedFile); // 선택된 파일을 상태에 저장
        }
    };

    const uploadAvatar = () => {
        const memberId = user.memberId; // user 객체에서 memberId 가져오기
        const token = localStorage.getItem('accessToken');

        const formData = new FormData();
        formData.append('avatarImage', file); // 파일을 FormData에 추가

        // 아바타 업로드 API 호출
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
                console.log('Avatar uploaded successfully:', data);
                // 업로드 후, getAvatar API를 호출하여 최신 아바타 URL을 가져옴
                return fetch(`http://localhost:8000/api/members/${memberId}/avatar`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    },
                });
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch avatar URL');
                }
                return response.json();
            })
            .then(data => {
                // 서버에서 받은 아바타 URL로 업데이트
                setUser(prevUser => ({
                    ...prevUser,
                    avatar: data.data // 서버에서 반환된 아바타 URL
                }));
                // 파일 상태 초기화
                setFile(null);
            })
            .catch(error => {
                console.error('Error uploading avatar:', error);
                setError('아바타 이미지 업로드에 실패했습니다.');
            });
    };

    // user 객체 확인
    console.log("User object:", user); // 추가된 로그

    // avatar에서 파일명만 추출 및 URL 생성
    const avatarImagePath = user.avatar; // user.avatar로 변경
    console.log("User avatar:", avatarImagePath); // 수정된 로그

    // 경로에서 파일명 추출
    const avatarFileName = avatarImagePath ? avatarImagePath.split('/').pop() : null;

    // avatarFileName 로그 추가
    console.log("Extracted avatarFileName:", avatarFileName);

    // 기본 아바타 이미지 URL 설정
    const defaultAvatarUrl = 'http://localhost:8000/images/default-avatar.png';

    // URL 생성: user.avatar가 없을 경우 기본 이미지 사용
    const avatarUrl = avatarImagePath ? `http://localhost:8000/${avatarFileName}` : defaultAvatarUrl;

    return (
        <div className="p-6">
            <h1 className="text-2xl font-semibold mb-4 text-gray-800">회원 정보</h1>
            {error && <p className="text-red-500 mb-4">{error}</p>} {/* 에러 메시지 표시 */}
            <div className="bg-white p-6 rounded-lg shadow-md mb-4">
                <h2 className="text-lg font-semibold text-gray-800">사용자 정보</h2>
                <p className="text-gray-700"><strong>사용자 ID:</strong> {user.userId}</p>
                <p className="text-gray-700"><strong>이름:</strong> {user.name}</p>
                <p className="text-gray-700"><strong>이메일:</strong> {memberInfo.email}</p>
                <p className="text-gray-700"><strong>전화번호:</strong> {memberInfo.phone}</p>
                <p className="text-gray-700"><strong>역할:</strong> {user.role}</p>
                <div className="flex items-center mt-4">
                    <a href={avatarUrl} target="_blank" rel="noopener noreferrer"
                       onClick={() => console.log("Clicked avatar URL:", avatarUrl)}>
                        <img
                            src={avatarUrl} // URL이 없으면 기본 이미지 사용
                            alt="User Avatar"
                            className="w-24 h-24 rounded-full border-2 border-gray-300 cursor-pointer"
                        />
                    </a>
                    <div className="ml-4">
                        <p className="text-gray-800">아바타 이미지</p>
                    </div>
                </div>
                {/* 아바타 이미지 수정하기 버튼 */}
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
                        disabled={!file} // 파일이 선택되지 않으면 비활성화
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
                    disabled={isLoading} // 로딩 중 버튼 비활성화
                >
                    {isLoading ? '수정 중...' : '수정하기'}
                </button>
            </form>
        </div>
    );
};

export default MemberInfoPage;
