# op.coffee, alc/llsm/src/redux/

action = require './action'
util = require '../util'
sm_native = require '../sm_native'


start_sm = ->
  (dispatch, getState) ->
    $$state = getState()
    # check server
    server = $$state.getIn ['config', 'server']
    if (! server?) or (server.trim() is '')
      util.toast "ERROR: bad server: #{server}"
      return
    if server.indexOf(':') is -1
      util.toast "ERROR: no port in server: #{server}"
      return
    config_server_ip = server[...server.indexOf(':')].trim()
    port = Number.parseInt server[server.indexOf(':') + 1 ..]
    if Number.isNaN(port) or (port < 1) or (port > 65534)
      util.toast "ERROR: bad port: #{server}"
      return
    # TODO more checks on config

    # screen_size
    screen_size = $$state.getIn ['config', 'screen_size']
    i = screen_size.indexOf 'x'
    if i is -1
      util.toast "ERROR: bad screen_size: #{screen_size}"
      return
    x = Number.parseInt screen_size[...i]
    y = Number.parseInt screen_size[i + 1 ..]
    if Number.isNaN(x) or Number.isNaN(y)
      util.toast "ERROR: bad screen_size: #{screen_size}"
      return
    if (x < 1) or (y < 1) or (x > 65535) or (y > 65535)
      util.toast "ERROR: bad screen_size value: #{screen_size}"
      return
    # fps
    fps_text = $$state.getIn ['config', 'fps']
    fps = Number.parseInt fps_text
    if Number.isNaN fps
      util.toast "ERROR: bad fps: #{fps_text}"
      return
    if (fps < 1) or (fps > 1024)
      util.toast "ERROR: bad fps value: #{fps}"
      return

    # check pass, update state
    dispatch action.set_config_server("#{config_server_ip}:#{port}")
    dispatch action.set_config_screen_size("#{x}x#{y}")
    dispatch action.set_config_fps("#{fps}")
    # make config
    config = {
      server_ip: config_server_ip
      server_port: port
      screen_size: [x, y]
      fps
    }

    await sm_native.start_sm config
    # TODO check result ?
    dispatch action.set_is_running(true)

module.exports = {
  start_sm  # thunk
}
