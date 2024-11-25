package org.example.propertyservice.service

import io.mockk.coEvery
import io.mockk.coVerify
import org.example.propertyservice.util.DefaultDtoCreator
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import org.example.propertyservice.mapper.PropertyMapper
import org.example.propertyservice.repository.PropertyRepository
import org.example.propertyservice.util.DefaultDocumentCreator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
@SpringBootTest
class PropertyServiceTest {

    @MockK
    private lateinit var propertyRepository: PropertyRepository

    @MockK
    private lateinit var propertyMapper: PropertyMapper

    @InjectMockKs
    private lateinit var propertyService: PropertyService

    @Autowired
    private lateinit var defaultDtoCreator: DefaultDtoCreator

    @Autowired
    private lateinit var defaultDocumentCreator: DefaultDocumentCreator

    @Test
    fun findAllTest() = runBlocking {
        //given
        val property = defaultDocumentCreator.createProperty()
        val propertyDto = defaultDtoCreator.createPropertyDto()
        val expectedProperties = listOf(property).asFlow()

        //when
        every { propertyRepository.findAll() } returns expectedProperties
        every { propertyMapper.toDto(property) } returns propertyDto
        every { propertyMapper.toDocument(propertyDto) } returns property
        val properties = propertyService.findAll()

        //then
        assertEquals("lfksalkf5aslkf2a", properties.first().ownerId)
        verify { propertyRepository.findAll() }
    }

    @Test
    fun findByIdTest() = runBlocking {
        //given
        val expectedProperty = defaultDocumentCreator.createProperty()
        val expectedPropertyDto = defaultDtoCreator.createPropertyDto()
        val propertyId = "sa;ldsa;ld;sald"

        //when
        coEvery { propertyRepository.findById(propertyId) } returns expectedProperty
        every { propertyMapper.toDto(expectedProperty) } returns expectedPropertyDto
        every { propertyMapper.toDocument(expectedPropertyDto) } returns expectedProperty
        val result = propertyService.findById(propertyId)

        //then
        assertEquals("67345edda795f93e969a08b6", result.id)
    }

    @Test
    fun saveTest() = runBlocking {
        //given
        val property = defaultDocumentCreator.createProperty()
        val propertyDto = defaultDtoCreator.createPropertyDto()
        val userId = "sa;ldsa;ld;sald"

        //when
        coEvery { propertyRepository.save(any()) } returns property
        every { propertyMapper.toDto(property) } returns propertyDto
        every { propertyMapper.toDocument(propertyDto) } returns property
        val savedPropertyDto = propertyService.create(propertyDto, userId)

        //then
        assertEquals(userId, savedPropertyDto.ownerId)
    }

    @Test
    fun updateTest() = runBlocking {
        //given
        val property = defaultDocumentCreator.createProperty()
        val propertyDto = defaultDtoCreator.createPropertyDto()

        //when
        coEvery { propertyRepository.save(any()) } returns property
        every { propertyMapper.toDto(property) } returns propertyDto
        every { propertyMapper.toDocument(propertyDto) } returns property
        propertyService.update(propertyDto)

        //then
        coVerify(exactly = 1) { propertyRepository.save(property) }
    }

    @Test
    fun deleteTest() = runBlocking {
        //given
        val propertyId = "67345edda795f93e969a08b6"
        val property = defaultDocumentCreator.createProperty()
        val userId = "lfksalkf5aslkf2a"

        //when
        coEvery { propertyRepository.deleteById(propertyId) } just runs
        coEvery { propertyRepository.findById(propertyId) } returns property
        propertyService.deleteById(propertyId, userId)

        //then
        coVerify(exactly = 1) { propertyRepository.deleteById(propertyId) }
    }
}
