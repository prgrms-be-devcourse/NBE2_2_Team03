import React from 'react';
import { Search } from 'lucide-react'; // 혹시 lucide-react에서 Search 아이콘을 사용하시는 경우

const SearchBar = () => {
    return (
        <div className="relative">
            <input
                type="text"
                placeholder="청원 검색..."
                className="w-full p-4 pr-12 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <Search className="absolute right-4 top-1/2 transform -translate-y-1/2 text-gray-400" />
        </div>
    );
};

export default SearchBar;
