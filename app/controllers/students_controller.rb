class StudentsController < ApplicationController
  before_action :set_student, only: [:show, :edit, :update, :destroy]

  def index
    @students = Student.all
  end

  def new
    @student = Student.new
  end

  def edit
  end

  def create
    @student = Student.new(student_params)
    if @student.save
      redirect_to :root, notice: 'Student was successfully created.'
    else
      render :new
    end
  end

  def import
    begin
      Student.import(params[:file])
      redirect_to :root, notice: 'Students successfully created from CSV.'
    rescue
      redirect_to :root, notice: 'Invalid CSV format.'
    end
  end

  def update
    if @student.update(student_params)
      redirect_to :root, notice: 'Student was successfully updated.'
    else
      render :edit
    end
  end

  def destroy
    if @student.orders.where(status: true).count == 0
      @student.destroy
      redirect_to :root, notice: 'Student was successfully destroyed.'
    else
      flash[:alert] = 'Students with active orders can not be deleted. Mark his/hers open orders as returned and try again.'
      redirect_to root_url
    end
  end

  def delete_all
    Student.delete_all
    redirect_to :root, notice: 'Students successfully destroyed.'
  end

  private
    def set_student
      @student = Student.find(params[:id])
    end

    def student_params
      params.require(:student).permit(:name, :email, :phone, :upc)
    end
end
