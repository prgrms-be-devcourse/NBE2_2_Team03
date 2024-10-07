import React, { useEffect, useState } from 'react';
import { Search } from 'lucide-react';
import SearchBar from './SearchBar.jsx';

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

const AllPetitionsPage = () => {
    const [petitions, setPetitions] = useState([]);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const [selectedCategory, setSelectedCategory] = useState("ALL");
    const [sortOrder, setSortOrder] = useState(sortOptions[0].value);
    const [searchQuery, setSearchQuery] = useState('');

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
                setPetitions(data.content);
                setTotalPages(data.totalPages);
            } catch (error) {
                console.error('Error fetching petitions:', error);
                setError('청원 데이터를 가져오는 데 실패했습니다.');
            } finally {
                setIsLoading(false);
            }
        };

        fetchPetitions();
    }, [currentPage, itemsPerPage, selectedCategory]);

    const handleSearch = async (query) => {
        setSearchQuery(query);
        setCurrentPage(0);
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
                setPetitions(data);
                setTotalPages(Math.ceil(data.length / itemsPerPage));
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
            fetchPetitions();
        }
    };

    const handlePageChange = (page) => {
        if (page >= 0 && page < totalPages) {
            setCurrentPage(page);
        }
    };

    const handleItemsPerPageChange = (event) => {
        setItemsPerPage(Number(event.target.value));
        setCurrentPage(0);
    };

    const handleCategoryChange = (event) => {
        setSelectedCategory(event.target.value);
        setCurrentPage(0);
    };

    const handleSortChange = (event) => {
        setSortOrder(event.target.value);
        setCurrentPage(0);
    };

    const sortedPetitions = () => {
        if (sortOrder === 'AGREE_COUNT') {
            return petitions;
        }
        return [...petitions].sort((a, b) => {
            if (sortOrder === 'LIKES_COUNT') {
                return b.likesCount - a.likesCount;
            } else if (sortOrder === 'EXPIRATION_DATE') {
                return new Date(a.endDate) - new Date(b.endDate);
            }
            return 0;
        });
    };

    return (
        <div className="p-6">
            <h2 className="text-2xl font-semibold mb-4 text-gray-800">전체 청원</h2>
            <SearchBar onSearch={handleSearch} />
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
