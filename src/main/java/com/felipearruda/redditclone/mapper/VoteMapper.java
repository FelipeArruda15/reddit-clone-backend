package com.felipearruda.redditclone.mapper;

import com.felipearruda.redditclone.dto.VoteDto;
import com.felipearruda.redditclone.model.Post;
import com.felipearruda.redditclone.model.User;
import com.felipearruda.redditclone.model.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoteMapper {

    @Mapping(target = "user", source = "user")
    Vote map(VoteDto voteDto, Post post, User user);
}
