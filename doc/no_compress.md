<!-- no_compress.md, alc/
-->

# No Compress
Achieve low latency by not compress video data.


## Video data formats

+ **Pixel format** (Color)

  | Name               | Byte per pixel | comment |
  | :----------------- | -------------: | :------ |
  | `RGB24` (`R8G8B8`) |         3 B/px | Red `8bit`, Green `8bit`, Blue `8bit` |
  | `RGB16` (`R5G6B5`) |         2 B/px | Red `5bit`, Green `6bit`, Blue `5bit` |
  | `Grey8`            |         1 B/px | Grey `8bit` |

  TODO: `YUV420`

+ **Frame size**

  | Name  | size (pixel) | Number of pixels per frame |
  | ----: | -----------: | -------------------------: |
  |    4K |  3840 x 2160 |          8.30M (8,294,400) |
  | 1080p |  1920 x 1080 |          2.08M (2,073,600) |
  |  720p |   1280 x 720 |             922K (921,600) |

+ **Frame rate**

  | Frame per second | 1 frame latency | 2 frames latency | 3 frames latency |
  | ---------------: | --------------: | ---------------: | ---------------: |
  |           60 fps |         16.7 ms |          33.4 ms |          50.0 ms |
  |           50 fps |         20.0 ms |          40.0 ms |          60.0 ms |
  |           30 fps |         33.4 ms |          66.7 ms |           100 ms |
  |           25 fps |         40.0 ms |          80.0 ms |           120 ms |
  |           15 fps |         66.7 ms |           134 ms |           200 ms |
  |           10 fps |          100 ms |           200 ms |           300 ms |

+ **Bandwidth**

  | Size  | FPS | Color | latency | frame size | bandwidth                   |
  | ----: | --: | :---- | ------: | ---------: | --------------------------: |
  |       |     |       | 0.06 ms |            | 40.6 Gbps (local network) *(1)* |
  |    4K |  60 | RGB24 | 16.7 ms |   23.74 MB | 11.95 Gbps (11,943,936,000) |
  |       |     |       |         |            |      10 Gbps (10G Ethernet) |
  |    4K |  50 | RGB24 | 20.0 ms |   23.74 MB |  9.954 Gbps (9,953,280,000) |
  |    4K |  60 | RGB16 | 16.7 ms |   15.83 MB |  7.963 Gbps (7,962,624,000) |
  |    4K |  50 | RGB16 | 20.0 ms |   15.83 MB |  6.636 Gbps (6,635,520,000) |
  |    4K |  30 | RGB24 | 33.4 ms |   23.74 MB |  5.972 Gbps (5,971,968,000) |
  |    4K |  25 | RGB24 | 40.0 ms |   23.74 MB |  4.977 Gbps (4,976,640,000) |
  |    4K |  30 | RGB16 | 33.4 ms |   15.83 MB |  3.982 Gbps (3,981,312,000) |
  |    4K |  60 | Grey8 | 16.7 ms |   7.911 MB |  3.982 Gbps (3,981,312,000) |
  |    4K |  25 | RGB16 | 40.0 ms |   15.83 MB |  3.318 Gbps (3,317,760,000) |
  |    4K |  50 | Grey8 | 20.0 ms |   7.911 MB |  3.318 Gbps (3,317,760,000) |
  |    4K |  15 | RGB24 | 66.7 ms |   23.74 MB |  2.986 Gbps (2,985,984,000) |
  | 1080p |  60 | RGB24 | 16.7 ms |   5.933 MB |  2.986 Gbps (2,985,984,000) |
  | 1080p |  50 | RGB24 | 20.0 ms |   5.933 MB |  2.489 Gbps (2,488,320,000) |
  |    4K |  10 | RGB24 |  100 ms |   23.74 MB |  1.991 Gbps (1,990,656,000) |
  |    4K |  15 | RGB16 | 66.7 ms |   15.83 MB |  1.991 Gbps (1,990,656,000) |
  |    4K |  30 | Grey8 | 33.4 ms |   7.911 MB |  1.991 Gbps (1,990,656,000) |
  | 1080p |  60 | RGB16 | 16.7 ms |   3.956 MB |  1.991 Gbps (1,990,656,000) |
  |    4K |  25 | Grey8 | 40.0 ms |   7.911 MB |  1.659 Gbps (1,658,880,000) |
  | 1080p |  50 | RGB16 | 20.0 ms |   3.956 MB |  1.659 Gbps (1,658,880,000) |
  | 1080p |  30 | RGB24 | 33.4 ms |   5.933 MB |  1.493 Gbps (1,492,992,000) |
  |    4K |  10 | RGB16 |  100 ms |   15.83 MB |  1.328 Gbps (1,327,104,000) |
  |  720p |  60 | RGB24 | 16.7 ms |   2.637 MB |  1.328 Gbps (1,327,104,000) |
  | 1080p |  25 | RGB24 | 40.0 ms |   5.933 MB |  1.245 Gbps (1,244,160,000) |
  |  720p |  50 | RGB24 | 20.0 ms |   2.637 MB |  1.106 Gbps (1,105,920,000) |
  |       |     |       |         |            |        1 Gbps (1G Ethernet) |
  |    4K |  15 | Grey8 | 66.7 ms |   7.911 MB |    995.4 Mbps (995,328,000) |
  | 1080p |  30 | RGB16 | 33.4 ms |   3.956 MB |    995.4 Mbps (995,328,000) |
  | 1080p |  60 | Grey8 | 16.7 ms |   1.978 MB |    995.4 Mbps (995,328,000) |
  |  720p |  60 | RGB16 | 16.7 ms |   1.758 MB |    884.8 Mbps (884,736,000) |
  | 1080p |  25 | RGB16 | 40.0 ms |   3.956 MB |    829.5 Mbps (829,440,000) |
  | 1080p |  50 | Grey8 | 20.0 ms |   1.978 MB |    829.5 Mbps (829,440,000) |
  | 1080p |  15 | RGB24 | 66.7 ms |   5.933 MB |    746.5 Mbps (746,496,000) |
  |  720p |  50 | RGB16 | 20.0 ms |   1.758 MB |    737.3 Mbps (737,280,000) |
  |    4K |  10 | Grey8 |  100 ms |   7.911 MB |    663.6 Mbps (663,552,000) |
  |  720p |  30 | RGB24 | 33.4 ms |   2.637 MB |    663.6 Mbps (663,552,000) |
  |  720p |  25 | RGB24 | 40.0 ms |   2.637 MB |    553.0 Mbps (552,960,000) |
  | 1080p |  10 | RGB24 |  100 ms |   5.933 MB |    497.7 Mbps (497,664,000) |
  | 1080p |  15 | RGB16 | 66.7 ms |   3.956 MB |    497.7 Mbps (497,664,000) |
  | 1080p |  30 | Grey8 | 33.4 ms |   1.978 MB |    497.7 Mbps (497,664,000) |
  |  720p |  30 | RGB16 | 33.4 ms |   1.758 MB |    442.4 Mbps (442,368,000) |
  |  720p |  60 | Grey8 | 16.7 ms |   900.0 KB |    442.4 Mbps (442,368,000) |
  | 1080p |  25 | Grey8 | 40.0 ms |   1.978 MB |    414.8 Mbps (414,720,000) |
  |  720p |  25 | RGB16 | 40.0 ms |   1.758 MB |    368.7 Mbps (368,640,000) |
  |  720p |  50 | Grey8 | 20.0 ms |   900.0 KB |    368.7 Mbps (368,640,000) |
  | 1080p |  10 | RGB16 |  100 ms |   3.956 MB |    331.8 Mbps (331,776,000) |
  |  720p |  15 | RGB24 | 66.7 ms |   2.637 MB |    331.8 Mbps (331,776,000) |
  |       |     |       | 0.60 ms |            | 251 Mbps (android usb network) *(2)* |
  | 1080p |  15 | Grey8 | 66.7 ms |   1.978 MB |    248.9 Mbps (248,832,000) |
  |  720p |  10 | RGB24 |  100 ms |   2.637 MB |    221.2 Mbps (221,184,000) |
  |  720p |  15 | RGB16 | 66.7 ms |   1.758 MB |    221.2 Mbps (221,184,000) |
  |  720p |  30 | Grey8 | 33.4 ms |   900.0 KB |    221.2 Mbps (221,184,000) |
  |  720p |  25 | Grey8 | 40.0 ms |   900.0 KB |    184.4 Mbps (184,320,000) |
  | 1080p |  10 | Grey8 |  100 ms |   1.978 MB |    165.9 Mbps (165,888,000) |
  |  720p |  10 | RGB16 |  100 ms |   1.758 MB |    147.5 Mbps (147,456,000) |
  |  720p |  15 | Grey8 | 66.7 ms |   900.0 KB |    110.6 Mbps (110,592,000) |
  |       |     |       |         |            |    100 Mbps (100M Ethernet) |
  |  720p |  10 | Grey8 |  100 ms |   900.0 KB |     73.73 Mbps (73,728,000) |
  |       |     |       |         |            | 39.9 Mbps (android usb adb) *(3)* |

(1) local network: 127.0.0.1/TCP, iperf3. CPU: i5-6200U, memory: DDR3-1600.
    Linux 4.15.3

(2) android usb network: Android 6.0, iperf3/TCP, USB 2.0 (Redmi Note 4X nikel)
    (usb tethering)

(3) android usb adb: Android 6.0, `adb reverse`, iperf3/TCP, USB 2.0


TODO


last_update: `2018-02-17`

<!-- end no_compress.md -->
