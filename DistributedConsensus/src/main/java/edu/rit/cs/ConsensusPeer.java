package edu.rit.cs;

public class ConsensusPeer {
    enum PeerType {
        Leader, Follower, Candidate
    }

    private int peerID=-1;
    private PeerType peerType=PeerType.Follower;

}
