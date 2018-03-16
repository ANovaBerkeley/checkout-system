module UsersHelper
    def get_students
        Student.all.map do |student| [student.name, student.id] 
        end
    end

    def get_mentors
        Mentor.all.map do |mentor| [mentor.name, mentor.id] 
        end
    end
end
