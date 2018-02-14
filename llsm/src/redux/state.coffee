# state.coffee, alc/llsm/src/redux/

init_state = {
  is_running: false

  config: {
    server: '127.0.0.1:4321'
    screen_size: '1280x720'
    fps: '30'
  }
}

module.exports = init_state
