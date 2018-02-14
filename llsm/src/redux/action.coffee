# action.coffee, alc/llsm/src/redux/

A_SET_IS_RUNNING = 'a_set_is_running'

A_SET_CONFIG_SERVER = 'a_set_config_server'
A_SET_CONFIG_SCREEN_SIZE = 'a_set_config_screen_size'
A_SET_CONFIG_FPS = 'a_set_config_fps'


set_is_running = (bool) ->
  {
    type: A_SET_IS_RUNNING
    payload: bool
  }

set_config_server = (text) ->
  {
    type: A_SET_CONFIG_SERVER
    payload: text
  }

set_config_screen_size = (text) ->
  {
    type: A_SET_CONFIG_SCREEN_SIZE
    payload: text
  }

set_config_fps = (text) ->
  {
    type: A_SET_CONFIG_FPS
    payload: text
  }

module.exports = {
  A_SET_IS_RUNNING
  A_SET_CONFIG_SERVER
  A_SET_CONFIG_SCREEN_SIZE
  A_SET_CONFIG_FPS

  set_is_running
  set_config_server
  set_config_screen_size
  set_config_fps
}
