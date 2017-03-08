--
-- Created by IntelliJ IDEA.
-- User: hill
-- Date: 17/2/14
-- Time: 下午5:26
-- To change this template use File | Settings | File Templates.
--

--function stylizer(opt)
--
--    print("counter_test stylizer")
--
--    if (opt.input_image == '') and (opt.input_dir == '') then
--        error('Must give exactly one of -input_image or -input_dir')
--    end
--end
require 'xlua'

function testpng()
--    print("----testlingshan")
--    if not xlua.require 'liblingshan' then
--        print("----require failed")
--    else
--        print("----require success")
--    end

    print("----test libluajit_png")
    if not xlua.require 'libluajit_png' then
        print("----require failed")
    else
        print("----require success")
    end
end

print("lingshan")