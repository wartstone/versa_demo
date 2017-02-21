
#include <TH.h>
#include <luaT.h>

#include "ApkFile.c"
#include "include/luaT.h"
#include "include/lua.h"

DLL_EXPORT int luaopen_libtorchandroid(lua_State *L)
{
  
  torch_ApkFile_init(L);

  return 1;
}
