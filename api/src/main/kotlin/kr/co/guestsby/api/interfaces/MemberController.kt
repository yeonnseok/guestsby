package kr.co.guestsby.api.interfaces

import kr.co.guestsby.api.domain.member.MemberService
import kr.co.guestsby.database.document.Member
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberService: MemberService,
) {
    @GetMapping("/{id}")
    suspend fun getMember(
        @PathVariable id: String,
    ): ResponseEntity<Member> {
        val member = memberService.getMember(id)
        return ResponseEntity.ok(member)
    }
}
