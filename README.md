[![progress-banner](https://backend.codecrafters.io/progress/bittorrent/ea8b4227-90f0-42e0-aee7-ea3bc69b52c9)](https://app.codecrafters.io/users/codecrafters-bot?r=2qF)

This is a starting point for Java solutions to the
["Build Your Own BitTorrent" Challenge](https://app.codecrafters.io/courses/bittorrent/overview).

In this challenge, you’ll build a BitTorrent client that's capable of parsing a
.torrent file and downloading a file from a peer. Along the way, we’ll learn
about how torrent files are structured, HTTP trackers, BitTorrent’s Peer
Protocol, pipelining and more.

**Note**: If you're viewing this repo on GitHub, head over to
[codecrafters.io](https://codecrafters.io) to try the challenge.

# Passing the first stage

The entry point for your BitTorrent implementation is in
`src/main/java/Main.java`. Study and uncomment the relevant code, and push your
changes to pass the first stage:

```sh
git commit -am "pass 1st stage" # any msg
git push origin master
```

Time to move on to the next stage!

# Stage 2 & beyond

Note: This section is for stages 2 and beyond.

1. Ensure you have `java (1.8)` installed locally
1. Run `./your_bittorrent.sh` to run your program, which is implemented in
   `src/main/java/Main.java`.
1. Commit your changes and run `git push origin master` to submit your solution
   to CodeCrafters. Test output will be streamed to your terminal.


## Some of the Reasons for Incorrect SHA-1 Hash of Info dictionary

**Hint 1**: If you've forgotten, let me remind you that while encoding the `info` dictionary, the keys should be in **lexicographical order**.

**Hint 2**: The value of the `pieces` key is a **byte string** (an array of bytes). Trying to typecast it to a `String` can lead to data loss, which results in an incorrect hash output.

So, take care of this while decoding the string initially (**Stage 1**) and later in the current stage (**Stage 6**) while encoding these.

**Note**: It’s up to you to research and decide which data type to use to prevent data loss.
