module UsersHelper
    def get_members 
        Member.all.map do |member| [member.name, member.id] 
        end
    end
end
