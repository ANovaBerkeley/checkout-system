module UsersHelper
    def get_students
        Student.all.map do |student| [student.name, student.id] 
        end
    end
end
