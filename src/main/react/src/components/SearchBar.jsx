import React from 'react';
import { Search } from 'lucide-react'; // lucide-react에서 Search 아이콘 사용

const SearchBar = () => {
    return (
        <div className="relative">
            <input
                type="text"
                placeholder="청원 검색..."
                className="w-full p-4 pr-12 rounded-full border border-gray-300 bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400" // 배경색과 텍스트 색상 조정
            />
            <Search className="absolute right-4 top-1/2 transform -translate-y-1/2 text-gray-500" />
        </div>
    );
};

export default SearchBar;
