import React, { useEffect, useState } from 'react';
import { Search } from 'lucide-react';

const categories = [
    { value: "ALL", label: "전체" },
    { value: "POLITICS", label: "정치/선거/국회운영" },
    { value: "INVESTIGATION", label: "수사/법무/사법제도" },
    { value: "FINANCE", label: "재정/세제/금융/예산" },
    { value: "CONSUMER", label: "소비자/공정거래" },
    { value: "EDUCATION", label: "교육" },
    { value: "SCIENCE", label: "과학기술/정보통신" },
    { value: "DIPLOMACY", label: "외교/통일/국방/안보" },
    { value: "DISASTER", label: "재난/안전/환경" },
    { value: "ADMINISTRATION", label: "행정/지방자치" },
    { value: "CULTURE", label: "문화/체육/관광/언론" },
    { value: "AGRICULTURE", label: "농업/임업/수산업/축산업" },
    { value: "INDUSTRY", label: "산업/통상" },
    { value: "HEALTHCARE", label: "보건의료" },
    { value: "WELFARE", label: "복지/보훈" },
    { value: "LAND", label: "국토/해양/교통" },
    { value: "HUMAN", label: "인권/성평등/노동" },
    { value: "LOW_BIRTHRATE", label: "저출산/고령화/아동/청소년/가족" },
    { value: "OTHERS", label: "기타" },
];

const sortOptions = [
    { value: 'AGREE_COUNT', label: '동의자 수' },
    { value: 'LIKES_COUNT', label: '좋아요 수' },
    { value: 'EXPIRATION_DATE', label: '만료일' }
];

const SearchBar = ({ onSearch }) => {
    const [searchTerm, setSearchTerm] = useState('');

    const handleSearch = () => {
        onSearch(searchTerm);
    };

    return (
        <div className="relative mb-4">
            <input
                type="text"
                placeholder="청원 검색..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                        handleSearch();
                    }
                }}
                className="w-full p-4 pr-12 rounded-full border border-gray-300 bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400"
            />
            <Search className="absolute right-4 top-1/2 transform -translate-y-1/2 text-gray-500" onClick={handleSearch} />
        </div>
    );
};

const AllPetitionsPage = () => {
    const [petitions, setPetitions] = useState([]);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(0); // 페이지 0부터 시작
    const [totalPages, setTotalPages] = useState(0);
    const [itemsPerPage, setItemsPerPage] = useState(10); // 페이지당 아이템 수
    const [selectedCategory, setSelectedCategory] = useState("ALL"); // 기본 카테고리
    const [sortOrder, setSortOrder] = useState(sortOptions[0].value); // 기본 정렬 기준
    const [searchQuery, setSearchQuery] = useState(''); // 검색어 상태

    useEffect(() => {
        const fetchPetitions = async () => {
            setIsLoading(true);
            try {
                const response = await fetch(`http://localhost:8000/api/petitions?page=${currentPage}&size=${itemsPerPage}&category=${selectedCategory}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
                if (!response.ok) {
                    throw new Error('네트워크 응답이 좋지 않습니다.');
                }
                const data = await response.json();
                setPetitions(data.content); // 청원 내용 설정
                setTotalPages(data.totalPages); // 전체 페이지 수 설정
            } catch (error) {
                console.error('Error fetching petitions:', error);
                setError('청원 데이터를 가져오는 데 실패했습니다.');
            } finally {
                setIsLoading(false);
            }
        };

        fetchPetitions();
    }, [currentPage, itemsPerPage, selectedCategory]); // 필터와 정렬 기준 변경 시 데이터 재요청

    const handleSearch = async (query) => {
        setSearchQuery(query);
        setCurrentPage(0); // 검색 시 첫 페이지로 초기화

        if (query) {
            setIsLoading(true);
            try {
                const response = await fetch(`http://localhost:8000/api/petitions/search?query=${query}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
                if (!response.ok) {
                    throw new Error('네트워크 응답이 좋지 않습니다.');
                }
                const data = await response.json();
                setPetitions(data); // 검색된 청원 내용 설정
                setTotalPages(Math.ceil(data.length / itemsPerPage)); // 전체 페이지 수 설정
            } catch (error) {
                console.error('Error fetching petitions:', error);
                setError('청원 검색에 실패했습니다.');
            } finally {
                setIsLoading(false);
            }
        } else {
            // 검색어가 없을 경우 원래 청원 목록으로 돌아가기
            const response = await fetch(`http://localhost:8000/api/petitions?page=${currentPage}&size=${itemsPerPage}&category=${selectedCategory}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            const data = await response.json();
            setPetitions(data.content);
            setTotalPages(data.totalPages);
        }
    };

    const handlePageChange = (page) => {
        if (page >= 0 && page < totalPages) { // 페이지 수가 0 이상, 전체 페이지 수 미만일 때만 변경
            setCurrentPage(page);
        }
    };

    const handleItemsPerPageChange = (event) => {
        setItemsPerPage(Number(event.target.value));
        setCurrentPage(0); // 페이지 수가 변경되면 첫 페이지로 초기화
    };

    const handleCategoryChange = (event) => {
        setSelectedCategory(event.target.value);
        setCurrentPage(0); // 카테고리 변경 시 첫 페이지로 초기화
    };

    const handleSortChange = (event) => {
        setSortOrder(event.target.value);
        setCurrentPage(0); // 정렬 기준 변경 시 첫 페이지로 초기화
    };

    const sortedPetitions = () => {
        // 동의자 수로 정렬할 경우 기존 순서 유지
        if (sortOrder === 'AGREE_COUNT') {
            return petitions; // 기존 순서 유지
        }

        // 좋아요 수 또는 만료일로 정렬
        return [...petitions].sort((a, b) => {
            if (sortOrder === 'LIKES_COUNT') {
                return b.likesCount - a.likesCount; // 내림차순 정렬
            } else if (sortOrder === 'EXPIRATION_DATE') {
                return new Date(a.endDate) - new Date(b.endDate); // 오름차순 정렬
            }
            return 0; // 기본값
        });
    };

    return (
        <div className="p-6">
            <h2 className="text-2xl font-semibold mb-4 text-gray-800">전체 청원</h2>
            <SearchBar onSearch={handleSearch}/>
            <div className="flex items-center mb-4">
                <label htmlFor="category" className="mr-2 text-black">카테고리:</label>
                <select
                    id="category"
                    value={selectedCategory}
                    onChange={handleCategoryChange}
                    className="border rounded p-2 bg-white text-black"
                >
                    {categories.map((category) => (
                        <option key={category.value} value={category.value}>{category.label}</option>
                    ))}
                </select>
                <label htmlFor="sort" className="ml-4 mr-2 text-black">정렬 기준:</label>
                <select
                    id="sort"
                    value={sortOrder}
                    onChange={handleSortChange}
                    className="border rounded p-2 bg-white text-black"
                >
                    {sortOptions.map((option) => (
                        <option key={option.value} value={option.value}>{option.label}</option>
                    ))}
                </select>
            </div>
            {isLoading && <p className="text-gray-600">로딩 중...</p>}
            {error && <p className="text-red-500">{error}</p>}
            {petitions.length === 0 ? (
                <p className="text-gray-600">데이터가 없습니다.</p>
            ) : (
                <ul>
                    {sortedPetitions().map((petition) => (
                        <li key={petition.petitionId} className="mb-4 p-4 bg-white rounded shadow">
                            <h3 className="text-xl font-semibold text-gray-800">{petition.title}</h3>
                            <p className="text-gray-600">시작일: {new Date(petition.startDate).toLocaleDateString()}</p>
                            <p className="text-gray-600">만료일: {new Date(petition.endDate).toLocaleDateString()}</p>
                            <p className="text-gray-600">카테고리: {petition.category}</p>
                            <p className="text-gray-600">좋아요 수: {petition.likesCount}</p>
                            <p className="text-gray-600">관심 수: {petition.interestCount}</p>
                            <p className="text-gray-600">동의 수: {petition.agreeCount}</p>
                        </li>
                    ))}
                </ul>
            )}
            <div className="flex items-center justify-between mt-4">
                <button
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 0}
                    className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
                >
                    이전
                </button>
                <div className="flex items-center">
                    {Array.from({ length: totalPages }, (_, index) => (
                        <button
                            key={index}
                            onClick={() => handlePageChange(index)}
                            className={`mx-1 px-3 py-1 rounded ${currentPage === index ? 'bg-blue-600 text-white' : 'bg-gray-200 text-black hover:bg-blue-400'}`}
                        >
                            {index + 1} {/* 페이지 번호 표시 */}
                        </button>
                    ))}
                </div>
                <button
                    onClick={() => handlePageChange(currentPage + 1)}
                    disabled={currentPage + 1 === totalPages}
                    className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
                >
                    다음
                </button>
            </div>
            <div className="mt-4 flex">
                <label htmlFor="itemsPerPage" className="mr-2 text-black">페이지당 게시물 수:</label>
                <select
                    id="itemsPerPage"
                    value={itemsPerPage}
                    onChange={handleItemsPerPageChange}
                    className="border rounded p-2 bg-white text-black"
                >
                    <option value={5}>5</option>
                    <option value={10}>10</option>
                    <option value={20}>20</option>
                    <option value={50}>50</option>
                </select>
            </div>
        </div>
    );
};

export default AllPetitionsPage;
