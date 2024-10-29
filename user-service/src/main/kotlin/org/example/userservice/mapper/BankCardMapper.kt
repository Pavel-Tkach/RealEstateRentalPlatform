package org.example.userservice.mapper

import org.example.userservice.entity.BankCard
import org.example.userservice.dto.BankCardDto
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface BankCardMapper {

    fun toDto(bankCard: BankCard): BankCardDto

    fun toDocument(bankCardDto: BankCardDto): BankCard
}
