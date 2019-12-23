package com.example.flatmaintenance.Interface;

import com.example.flatmaintenance.Model.Block;

import java.util.List;

public interface IBlockLoadListener {
    void onBlockLoadSuccess(List<Block> blockList);
    void onBlockLoadFailed(String message);
}
