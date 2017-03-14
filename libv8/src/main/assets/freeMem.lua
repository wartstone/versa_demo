--
-- Created by IntelliJ IDEA.
-- User: hill
-- Date: 17/3/9
-- Time: 下午6:24
-- To change this template use File | Settings | File Templates.
--
local loaders = {}

function freeMem()
    print("freeMem begin")

    local ok, checkpoint = pcall(function() return torch.load(opt.model) end)
    if not ok then
        print('ERROR: Could not load model from ' .. opt.model)
        print('You may need to download the pretrained models by running')
        print('bash models/download_style_transfer_models.sh')
        return
    end

--    table.insert(loaders, checkpoint)
end

function pre_load(opt)
    print('freeMem: pre_load begin')

    local file = io.open(opt.model, "r")
    local arr = {}
    for line in file:lines() do
        table.insert(arr, line)
    end

    table.insert(loaders, arr)

    arr = nil
    file = nil

    collectgarbage("collect")
    collectgarbage("collect")
    collectgarbage("collect")
    local mem_end = collectgarbage("count")
    print('--MEM END : ' .. mem_end)

    print('freeMem: pre_load end')
end

function post_stylize(opt)
    print('freeMem: post_stylize begin')

--    local index = opt.index
--    local checkpoint = loaders[index]
--
--    checkpoint = nil
--    loaders = nil

    -- 内存调试
    collectgarbage("collect")
    collectgarbage("collect")
    collectgarbage("collect")
    local mem_end = collectgarbage("count")
    print('--MEM END : ' .. mem_end)

    print('freeMem: post_stylize end')
end