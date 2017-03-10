--
-- Created by IntelliJ IDEA.
-- User: hill
-- Date: 17/3/9
-- Time: 下午6:24
-- To change this template use File | Settings | File Templates.
--

function freeMem()
    print("freeMem begin")

    local ok, checkpoint = pcall(function() return torch.load(opt.model) end)
    if not ok then
        print('ERROR: Could not load model from ' .. opt.model)
        print('You may need to download the pretrained models by running')
        print('bash models/download_style_transfer_models.sh')
        return
    end

    table.insert(loaders, checkpoint)
end